package com.apimobilestore.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apimobilestore.dto.request.PermissionRequest;
import com.apimobilestore.dto.response.ApiResponse;
import com.apimobilestore.dto.response.PermissionResponse;
import com.apimobilestore.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/permission")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionController {
	PermissionService permissionService;
	
	@PostMapping("/create")
	ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest){
		return ApiResponse.<PermissionResponse>builder()
				.result(permissionService.createPermission(permissionRequest))
				.build();
	}
	
	@GetMapping("/getAllPermission")
	ApiResponse<List<PermissionResponse>> getAllRole(){
		return ApiResponse.<List<PermissionResponse>>builder()
				.result(permissionService.getAllPermissions())
				.build();
	}
	
	@DeleteMapping("/delete/{permission}")
	ApiResponse<?> deleteUser(@PathVariable String permission){
		permissionService.deletePermission(permission);
		return ApiResponse.builder()
				.message(String.format("Permission %s has been deleted", permission))
				.build();
	}
}
