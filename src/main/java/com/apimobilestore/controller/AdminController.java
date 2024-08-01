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

import com.apimobilestore.dto.request.CommentRequest;
import com.apimobilestore.dto.request.PostRequest;
import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.ApiResponse;
import com.apimobilestore.dto.response.CommentResponse;
import com.apimobilestore.dto.response.PostResponse;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.service.AdminService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminController {
	AdminService adminService;
	
	// FOR USER ACCOUNT MANEGEMENT
	@GetMapping("/user/{username}")
	ApiResponse<UserResponse> getUser(@PathVariable String name){
		return ApiResponse.<UserResponse>builder()
				.result(adminService.getInfo(name))
				.build();
	}
	
	@GetMapping("/user")
	ApiResponse<List<UserResponse>> getAllUsers(){
		return ApiResponse.<List<UserResponse>>builder()
				.result(adminService.getAllUser())
				.build();
	}
	
	@DeleteMapping("/user/{username}")
	ApiResponse<?> deleteUser(@PathVariable String name){
		adminService.deleteUser(name);
		return ApiResponse.builder()
				.message("Account has been deleted")
				.build();
	}
	
	@PutMapping("/user/{username}")
	ApiResponse<UserResponse> updateUser(@PathVariable String name, @RequestBody UserUpdate request){
		return ApiResponse.<UserResponse>builder()
				.result(adminService.updateUser(name, request))
				.build();
	}
	
	// FOR POST MANAGEMENT
	@PostMapping("/post/{username}")
	ApiResponse<PostResponse> createPost(@PathVariable String name, @RequestBody PostRequest request){
		return ApiResponse.<PostResponse>builder()
				.result(adminService.createPost(name, request))
				.build();
	}
	
	@PutMapping("/post/{postId}")
	ApiResponse<PostResponse> updatePost(@PathVariable String postId, @RequestBody PostRequest request){
		return ApiResponse.<PostResponse>builder()
				.result(adminService.updatePost(postId, request))
				.build();
	}
	
	@DeleteMapping("/post/{postId}")
	ApiResponse<?> deletePost(@PathVariable String postId){
		adminService.deletePost(postId);
		return ApiResponse.builder()
				.message("This post has been deleted")
				.build();
	}
	
	// FOR COMMENT MANEGEMENT
	@PostMapping("/comment/{userId}/{postId}")
	ApiResponse<CommentResponse> createComment(@PathVariable String userId,@PathVariable String postId, @RequestBody CommentRequest request){
		return ApiResponse.<CommentResponse>builder()
				.result(adminService.createComment(userId, postId, request))
				.build();
	}
	
	@PutMapping("/comment/{cmtId}")
	ApiResponse<CommentResponse> updateComment(@PathVariable String cmtId, @RequestBody CommentRequest request){
		return ApiResponse.<CommentResponse>builder()
				.result(adminService.updateComment(cmtId, request))
				.build();
	}
	
	@DeleteMapping("/comment/{cmtId}")
	ApiResponse<?> updateComment(@PathVariable String cmtId){
		adminService.deleteComment(cmtId);
		return ApiResponse.builder()
				.message("This comment has been deleted")
				.build();
	}
}
