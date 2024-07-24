package com.apimobilestore.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
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
	UserResponse updateUser(String username,UserUpdate request);
	UserResponse getInfo(String username);
	List<UserResponse> getAllUser();
	void deleteUser(String username);
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
		User user = mapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(Roles.USER.name()).ifPresent(roles::add);
        

        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
		return mapper.toUserResponse(user);
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

	@Override
	@Transactional(readOnly = true)
	public UserResponse getInfo(String username) {
		return mapper.toUserResponse(userRepository.findByUsername(username)
				.orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED)));
	}
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getAllUser() {
		List<User> users = userRepository.findAll();
	    return users.stream()
	                .map(mapper::toUserResponse)
	                .collect(Collectors.toList());
	}
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	@Transactional
	public void deleteUser(String username) {
		userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        userRepository.deleteByUsername(username);
	}

	
}
