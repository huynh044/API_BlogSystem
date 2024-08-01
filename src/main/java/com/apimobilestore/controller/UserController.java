package com.apimobilestore.controller;

import com.apimobilestore.dto.request.UserCreation;
import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.ApiResponse;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.service.UserService;

import jakarta.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Validated
public class UserController {
	UserService service;
	
	@PostMapping
	ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreation request){
		return ApiResponse.<UserResponse>builder()
				.result(service.createUser(request))
				.build();
	}
	
	@PutMapping
	ApiResponse<UserResponse> updateUser(@Valid @RequestBody UserUpdate request){
		return ApiResponse.<UserResponse>builder()
				.result(service.updateUser(request))
				.build();
	}
	
	
	@DeleteMapping
	ApiResponse<?> deleteUser(){
		service.deleteUser();
		return ApiResponse.builder()
				.message("Account has been deleted")
				.build();
	}
	
	@GetMapping
	ApiResponse<UserResponse> getMyInfo(){
		return ApiResponse.<UserResponse>builder()
				.result(service.getMyInfo())
				.build();
	}
	
	
}
