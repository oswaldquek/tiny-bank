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
public class TransfersIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userId1;
    private String userId2;

    @BeforeEach
    void setUp() throws Exception {
        Map<String, String> createUser1Request = Map.of(
                "firstName", "Harry",
                "lastName", "Potter",
                "dateOfBirth", "1999-08-08",
                "address", "321 Privet Drive");

        MvcResult mvcResult = mvc.perform(post("/v1/user")
                        .content(objectMapper.writeValueAsString(createUser1Request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        userId1 = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        Map<String, String> createUser2Request = Map.of(
                "firstName", "Hermione",
                "lastName", "Granger",
                "dateOfBirth", "1998-07-07",
                "address", "123 Knowwhere");

        mvcResult = mvc.perform(post("/v1/user")
                        .content(objectMapper.writeValueAsString(createUser2Request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        userId2 = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
    }

    @Test
    void transferMoneySuccessfully() throws Exception {
        mvc.perform(get("/v1/user/" + userId2 + "/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId2)))
                .andExpect(jsonPath("$.balance", is(0)));

        mvc.perform(post("/v1/user/" + userId1 + "/account/add-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 1000))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId1)))
                .andExpect(jsonPath("$.balance", is(1000)));

        mvc.perform(post("/v1/transfer/" + userId1 + "/" + userId2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 500))))
                .andExpect(status().isOk());

        mvc.perform(get("/v1/user/" + userId1 + "/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId1)))
                .andExpect(jsonPath("$.balance", is(500)));

        mvc.perform(get("/v1/user/" + userId2 + "/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId2)))
                .andExpect(jsonPath("$.balance", is(500)));
    }

    @Test
    void unprocessableEntityIfNotEnoughMoneyToTransfer() throws Exception {
        mvc.perform(post("/v1/user/" + userId1 + "/account/add-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 1000))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId1)))
                .andExpect(jsonPath("$.balance", is(1000)));

        mvc.perform(post("/v1/transfer/" + userId1 + "/" + userId2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 1500))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void badRequestIfTransferringMoneyToSelf() throws Exception {
        mvc.perform(post("/v1/user/" + userId1 + "/account/add-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 1000))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId1)))
                .andExpect(jsonPath("$.balance", is(1000)));

        mvc.perform(post("/v1/transfer/" + userId1 + "/" + userId1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 500))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void badRequestIfTransferringMoneyToNonExistentPerson() throws Exception {
        mvc.perform(post("/v1/transfer/nonExistentPerson1/nonExistentPerson2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 500))))
                .andExpect(status().isBadRequest());
    }
}
