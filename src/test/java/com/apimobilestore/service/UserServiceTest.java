package com.apimobilestore.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.apimobilestore.dto.request.UserCreation;
import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.entity.Role;
import com.apimobilestore.entity.User;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.mapper.UserMapper;
import com.apimobilestore.repository.RoleRepository;
import com.apimobilestore.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestPropertySource("/test.properties")
@Slf4j
public class UserServiceTest {
	@MockBean
	UserRepository userRepository;
	@MockBean
	RoleRepository roleRepository;
	@MockBean
	UserMapper userMapper;
	@MockBean
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserService userService;
	
	private UserCreation userCreationRequest() {
    	return new UserCreation(
    			"huynh",
    			"huynh",
    			"huynh2004",
    			"huynh2004"
    	);
    }
    private UserResponse userResponse() {
    	return new UserResponse(
    			"123456",
    			"huynh",
    			"huynh",
    			"huynh2004",
    			"huynh2004",
    			null
    	);
    }
    
    private UserUpdate userUpdate() {
    	return new UserUpdate("huynh2004","huynh","huynh");
    }
    
    private User user = new User();
    private Role role = new Role();
	
	@Test
	void when_createUser_Service_runSucess() {
		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		when(userMapper.toUser(userCreationRequest())).thenReturn(user);
		when(passwordEncoder.encode(anyString())).thenReturn("encoded");
		when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
		when(userRepository.save(any())).thenReturn(user);
		when(userMapper.toUserResponse(user)).thenReturn(userResponse());
		
		var response = userService.createUser(userCreationRequest());
		
		Assertions.assertThat(response.getUid()).isEqualTo("123456");
        Assertions.assertThat(response.getUsername()).isEqualTo("huynh2004");
	}
	
	@Test
	void when_createUser_UserExisted_returnThrow() {
		when(userRepository.existsByUsername(anyString())).thenReturn(true);
		
		AppException thrown = assertThrows(AppException.class, () -> userService.createUser(userCreationRequest()));
        assertEquals(ErrorCode.USER_EXISTED, thrown.getErrorCode());
	}
	
	@Test
	@WithMockUser(username = "huynh2004", roles = "USER")
	void when_getMyInfo_returnSuccess() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(userMapper.toUserResponse(user)).thenReturn(userResponse());
		
		var response = userService.getMyInfo();
		
		Assertions.assertThat(response.getUid()).isEqualTo("123456");
	}
	
	@Test
	@WithMockUser(username = "huynh2004", roles = "USER")
	void when_getMyInfo_returnException() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));
		
		var exception = assertThrows(AppException.class, () -> userService.getMyInfo());
		
		Assertions.assertThat(exception.getMessage()).isEqualTo("User not existed");
	}
	
	@Test
	@WithMockUser(username = "huynh2004", roles = "USER")
	void when_updateUser_returnSucesc() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		userMapper.updateUser(user, userUpdate());
		when(passwordEncoder.encode(anyString())).thenReturn("encoded");
		when(userRepository.save(any())).thenReturn(user);
		when(userMapper.toUserResponse(user)).thenReturn(userResponse());
		
		
		var response = userService.updateUser(userUpdate());
		
		Assertions.assertThat(response.getUid()).isEqualTo("123456");
	}
	
	@Test
	@WithMockUser(username = "huynh2004", roles = "USER")
	void when_deleteUser_returnSuccess() {
		userRepository.deleteByUsername(anyString());
	}

}
