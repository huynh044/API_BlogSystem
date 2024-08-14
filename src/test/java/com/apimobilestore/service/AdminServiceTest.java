package com.apimobilestore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.apimobilestore.dto.request.CommentRequest;
import com.apimobilestore.dto.request.PostRequest;
import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.entity.User;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.mapper.CommentMapper;
import com.apimobilestore.mapper.PostMapper;
import com.apimobilestore.mapper.UserMapper;
import com.apimobilestore.repository.CommentRepository;
import com.apimobilestore.repository.PostRepository;
import com.apimobilestore.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestPropertySource("/test.properties")
@Slf4j
public class AdminServiceTest {
	@MockBean
	UserRepository userRepository;
	@MockBean
	UserMapper userMapper;
	@MockBean
	PostRepository postRepository;
	@MockBean
	PostMapper postMapper;
	@MockBean
	CommentRepository commentRepository;
	@MockBean
	CommentMapper commentMapper;
	
	@Autowired
	AdminService adminService;
	
	User user = new User();
	UserUpdate userUpdate() {
		return new UserUpdate("huynh2004","huynh","huynh");
	}
	UserResponse userResponse() {
    	return new UserResponse(
    			"123456",
    			"huynh",
    			"huynh",
    			"huynh2004",
    			"huynh2004",
    			null
    	);
    }
	PostRequest postRequest() {
		return new PostRequest("testTitle", "testContent");
	}
	CommentRequest commentRequest() {
		return new CommentRequest("testContent");
	}
	
	@Test
	@WithMockUser(username = "huynh2004", roles = "ADMIN")
	void when_getInfo_returnSuccess() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(userMapper.toUserResponse(any())).thenReturn(userResponse());
		
		var response = adminService.getInfo(anyString());
		
		Assertions.assertThat(response.getUid()).isEqualTo("123456");
	}
	
	@Test
	@WithMockUser(username = "huynh2004", roles = "ADMIN")
	void when_getInfo_returnNoOne() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		
		AppException exception = assertThrows(AppException.class, () -> adminService.getInfo(anyString()));
		assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
		verify(userRepository).findByUsername(anyString());
        verify(userMapper, never()).toUserResponse(any());
	}
}
