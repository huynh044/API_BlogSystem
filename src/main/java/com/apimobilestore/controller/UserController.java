package com.apimobilestore.controller;

import com.apimobilestore.dto.request.UserCreation;
import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.ApiResponse;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
	UserService service;
	
	@PostMapping("/create")
	ApiResponse<UserResponse> createUser(@RequestBody UserCreation request){
		return ApiResponse.<UserResponse>builder()
				.result(service.createUser(request))
				.build();
	}
	
	@PutMapping("/update")
	ApiResponse<UserResponse> updateUser(@RequestBody UserUpdate request){
		return ApiResponse.<UserResponse>builder()
				.result(service.updateUser(request))
				.build();
	}
	
	
	@DeleteMapping("/delete")
	ApiResponse<?> deleteUser(){
		service.deleteUser();
		return ApiResponse.builder()
				.message("Account has been deleted")
				.build();
	}
	
	@GetMapping("/getMyInfo")
	ApiResponse<UserResponse> getMyInfo(){
		return ApiResponse.<UserResponse>builder()
				.result(service.getMyInfo())
				.build();
	}
	
	
}
