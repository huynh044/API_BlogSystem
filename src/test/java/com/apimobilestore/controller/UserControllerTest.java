package com.apimobilestore.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.apimobilestore.configuration.Configuration;
import com.apimobilestore.dto.request.UserCreation;
import com.apimobilestore.dto.response.RoleResponse;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = Configuration.class)
@Slf4j
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    
    private UserCreation userCreationRequest() {
    	return new UserCreation(
    			"huynh",
    			"huynh",
    			"huynh2004",
    			"huynh2004"
    	);
    }
    
    private RoleResponse roleResponse() {
    	return new RoleResponse(
    			"USER",
    			"User role"
    			);
    }
    
    private Set<RoleResponse> roles(){
    	Set<RoleResponse> role = new HashSet<RoleResponse>();
    	role.add(roleResponse());
    	return role;
    }
    
    private UserResponse userResponse() {
    	return new UserResponse(
    			"123456",
    			"huynh",
    			"huynh",
    			"huynh2004",
    			"huynh2004",
    			roles()
    	);
    }

    @Test
    void when_createUser_validRequest_returnSuccessResponse() throws Exception {
        UserCreation request = userCreationRequest();
        UserResponse response = userResponse();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
        
        when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);
        
        mockMvc.perform(MockMvcRequestBuilders
        		.post("/api/user")
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.content(content))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
        .andExpect(MockMvcResultMatchers.jsonPath("$.result.uid").value("123456"))
        .andDo(print());
        		
    }
    
    @Test
    void when_createUser_invalidRequest_returnInValidRequestException() throws Exception {
    	UserCreation request = userCreationRequest();
    	request.setUsername("test");
        UserResponse response = userResponse();
        
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
        
        when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);
        
        mockMvc.perform(MockMvcRequestBuilders
        		.post("/api/user")
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.content(content))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 5 characters"))
        .andDo(print());
    }
    
}

