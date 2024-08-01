package com.apimobilestore.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apimobilestore.dto.request.RoleResquest;
import com.apimobilestore.dto.request.UpdateRoleRequest;
import com.apimobilestore.dto.response.ApiResponse;
import com.apimobilestore.dto.response.RoleResponse;
import com.apimobilestore.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/role")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleController {
	RoleService roleService;
	
	@PostMapping
	ApiResponse<RoleResponse> createRole (@RequestBody RoleResquest roleResquest){
		return ApiResponse.<RoleResponse>builder()
				.result(roleService.createResponse(roleResquest))
				.build();
	}
	
	@PutMapping
	ApiResponse<RoleResponse> updateRole (@RequestBody UpdateRoleRequest roleResquest){
		return ApiResponse.<RoleResponse>builder()
				.result(roleService.updateResponse(roleResquest))
				.build();
	}
	
	@GetMapping
	ApiResponse<List<RoleResponse>> getAllRole(){
		return ApiResponse.<List<RoleResponse>>builder()
				.result(roleService.getAllRole())
				.build();
	}
	
	@DeleteMapping("/{role}")
	ApiResponse<?> deleteUser(@PathVariable String role){
		roleService.deleteRole(role);
		return ApiResponse.builder()
				.message(String.format("Role %s has been deleted", role))
				.build();
	}
}
