package com.apimobilestore.service;

import java.util.HashSet;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apimobilestore.dto.request.UserCreation;
import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.entity.Role;
import com.apimobilestore.entity.User;
import com.apimobilestore.enums.Roles;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.mapper.UserMapper;
import com.apimobilestore.repository.RoleRepository;
import com.apimobilestore.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


public interface UserService {
	UserResponse createUser(UserCreation request);
	UserResponse updateUser(UserUpdate request);
	void deleteUser();
	UserResponse getMyInfo();
}
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserServiceImpl implements UserService{
	UserRepository userRepository;
	UserMapper mapper;
	PasswordEncoder passwordEncoder;
	RoleRepository roleRepository;
	

	@Override
	@Transactional
	public UserResponse createUser(UserCreation request) {
		if(userRepository.existsByUsername(request.getUsername())) {
			throw new AppException(ErrorCode.USER_EXISTED);
		}
		User user = mapper.toUser(request);
		
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(Roles.USER.name()).ifPresent(roles::add);
        roleRepository.findById(Roles.AUTHOR.name()).ifPresent(roles::add);
        roleRepository.findById(Roles.VIEWER.name()).ifPresent(roles::add);
        
        

        user.setRoles(roles);

		return mapper.toUserResponse(userRepository.save(user));
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('USER')")
	public UserResponse updateUser(UserUpdate request) {
		var context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
		mapper.updateUser(user, request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		return mapper.toUserResponse(userRepository.save(user));
	}
	

	@Override
	@PreAuthorize("hasRole('USER')")
	public UserResponse getMyInfo() {
		var context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
		return mapper.toUserResponse(user);
	}

	@Override
	@PreAuthorize("hasRole('USER')")
	@Transactional
	public void deleteUser() {
		var context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		userRepository.deleteByUsername(username);
		
	}

	
}
