package com.tinybank.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountActionsIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userId;

    @BeforeEach
    void setUp() throws Exception {
        Map<String, String> createUserRequest = Map.of(
                "firstName", "Harry",
                "lastName", "Potter",
                "dateOfBirth", "1999-08-08",
                "address", "321 Privet Drive");

        MvcResult mvcResult = mvc.perform(post("/v1/user")
                        .content(objectMapper.writeValueAsString(createUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        userId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    }

    @Test
    void depositMoneySuccessfully() throws Exception {
        Map<String, Object> transactionRequest = Map.of("amount", 1000);

        mvc.perform(post("/v1/user/" + userId + "/account/add-transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId)))
                .andExpect(jsonPath("$.balance", is(1000)));

        transactionRequest = Map.of("amount", 2000);

        mvc.perform(post("/v1/user/" + userId + "/account/add-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId)))
                .andExpect(jsonPath("$.balance", is(3000)));
    }

    @Test
    void withdrawMoneySuccessfully() throws Exception {
        Map<String, Object> transactionRequest = Map.of("amount", 1000);

        mvc.perform(post("/v1/user/" + userId + "/account/add-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk());

        transactionRequest = Map.of("amount", -500);

        mvc.perform(post("/v1/user/" + userId + "/account/add-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId)))
                .andExpect(jsonPath("$.balance", is(500)));
    }

    @Test
    void returnNotFoundIfUserNotFoundWhenAddingTransaction() throws Exception {
        Map<String, Object> transactionRequest = Map.of("amount", 1000);

        mvc.perform(post("/v1/user/userIdNotExist/account/add-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnUnprocessableContentIfNotEnoughMoney() throws Exception {
        Map<String, Object> transactionRequest = Map.of("amount", -500L);

        mvc.perform(post("/v1/user/" + userId + "/account/add-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void viewAccountBalance() throws Exception {
        mvc.perform(get("/v1/user/" + userId + "/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId)))
                .andExpect(jsonPath("$.balance", is(0)));

        Map<String, Object> transactionRequest = Map.of("amount", 1000);

        mvc.perform(post("/v1/user/" + userId + "/account/add-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk());

        mvc.perform(get("/v1/user/" + userId + "/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId)))
                .andExpect(jsonPath("$.balance", is(1000)));
    }
}
