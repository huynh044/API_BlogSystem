package com.apimobilestore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.entity.User;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.mapper.UserMapper;
import com.apimobilestore.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

public interface AdminService {
	UserResponse getInfo(String username);
	List<UserResponse> getAllUser();
	void deleteUser(String username);
	UserResponse updateUser(String username,UserUpdate request);
}
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
class AdminServiceImpl implements AdminService{
	UserRepository userRepository;
	UserMapper mapper;
	PasswordEncoder passwordEncoder;
	
	
	@Override
	@Transactional(readOnly = true)
	public UserResponse getInfo(String username) {
		return mapper.toUserResponse(userRepository.findByUsername(username)
				.orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED)));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getAllUser() {
		List<User> users = userRepository.findAll();
	    return users.stream()
	                .map(mapper::toUserResponse)
	                .collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	public void deleteUser(String username) {
		userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        userRepository.deleteByUsername(username);
	}
	
	@Override
	@Transactional
	public UserResponse updateUser(String username,UserUpdate request) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
		mapper.updateUser(user, request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		return mapper.toUserResponse(userRepository.save(user));
	}
}
