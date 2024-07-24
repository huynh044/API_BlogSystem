package com.apimobilestore.controller;

import com.apimobilestore.dto.request.UserCreation;
import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.ApiResponse;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.service.UserService;
import java.util.List;
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
	
	@PutMapping("/update/{username}")
	ApiResponse<UserResponse> updateUser(@PathVariable String username, @RequestBody UserUpdate request){
		return ApiResponse.<UserResponse>builder()
				.result(service.updateUser(username, request))
				.build();
	}
	
	@GetMapping("/info/{username}")
	ApiResponse<UserResponse> getInfo(@PathVariable String username){
		return ApiResponse.<UserResponse>builder()
				.result(service.getInfo(username))
				.build();
	}
	
	@GetMapping("/getAllUsers")
	ApiResponse<List<UserResponse>> getAllUsers(){
		return ApiResponse.<List<UserResponse>>builder()
				.result(service.getAllUser())
				.build();
	}
	
	@DeleteMapping("/delete/{username}")
	ApiResponse<?> deleteUser(@PathVariable String username){
		service.deleteUser(username);
		return ApiResponse.builder()
				.message("Account has been deleted")
				.build();
	}
	
	
}
