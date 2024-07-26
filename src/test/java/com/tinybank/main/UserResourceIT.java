package com.tinybank.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserResourceIT {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	private final Map<String, String> createUserRequest = Map.of(
				"firstName", "Harry",
				"lastName", "Potter",
				"dateOfBirth", "1999-08-08",
				"address", "321 Privet Drive");

	@Test
	void createUserSuccessfully() throws Exception {
		mvc.perform(post("/v1/user")
						.content(objectMapper.writeValueAsString(createUserRequest))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", startsWith("user_")))
				.andExpect(jsonPath("$.status", is("active")))
				.andExpect(jsonPath("$.firstName", is("Harry")))
				.andExpect(jsonPath("$.lastName", is("Potter")))
				.andExpect(jsonPath("$.dateOfBirth", is("1999-08-08")))
				.andExpect(jsonPath("$.address", is("321 Privet Drive")));
	}

	@Test
	void deactivateUserSuccessfully() throws Exception {
		MvcResult mvcResult = mvc.perform(post("/v1/user")
						.content(objectMapper.writeValueAsString(createUserRequest))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();

		String userId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

		mvc.perform(patch("/v1/user/" + userId + "/deactivate")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		mvc.perform(get("/v1/user/" + userId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(userId)))
				.andExpect(jsonPath("$.status", is("disabled")))
				.andExpect(jsonPath("$.firstName", is("Harry")))
				.andExpect(jsonPath("$.lastName", is("Potter")))
				.andExpect(jsonPath("$.dateOfBirth", is("1999-08-08")))
				.andExpect(jsonPath("$.address", is("321 Privet Drive")));
 	}
}
