package com.apimobilestore.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.ApiResponse;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.service.AdminService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminController {
	AdminService adminService;
	
	@GetMapping("/getUser/{username}")
	ApiResponse<UserResponse> getUser(@PathVariable String name){
		return ApiResponse.<UserResponse>builder()
				.result(adminService.getInfo(name))
				.build();
	}
	
	@GetMapping("/getAllUsers")
	ApiResponse<List<UserResponse>> getAllUsers(){
		return ApiResponse.<List<UserResponse>>builder()
				.result(adminService.getAllUser())
				.build();
	}
	
	@DeleteMapping("/delete/{username}")
	ApiResponse<?> deleteUser(@PathVariable String name){
		adminService.deleteUser(name);
		return ApiResponse.builder()
				.message("Account has been deleted")
				.build();
	}
	
	@PutMapping("/update/{username}")
	ApiResponse<UserResponse> updateUser(@PathVariable String name, @RequestBody UserUpdate request){
		return ApiResponse.<UserResponse>builder()
				.result(adminService.updateUser(name, request))
				.build();
	}
}
