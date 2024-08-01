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

import com.apimobilestore.dto.request.PostRequest;
import com.apimobilestore.dto.response.ApiResponse;
import com.apimobilestore.dto.response.PostResponse;
import com.apimobilestore.service.PostService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/post")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PostController {
	PostService postService;
	
	@PostMapping
	ApiResponse<PostResponse> createPost(@RequestBody PostRequest postRequest){
		return ApiResponse.<PostResponse>builder()
				.result(postService.createPost(postRequest))
				.build();
	}
	
	@PutMapping("/{postId}")
	ApiResponse<PostResponse> updatePost(@PathVariable String postId,@RequestBody PostRequest postRequest){
		return ApiResponse.<PostResponse>builder()
				.result(postService.updatePost(postId,postRequest))
				.build();
	}
	
	@GetMapping
	ApiResponse<List<PostResponse>> getAllPosts(){
		return ApiResponse.<List<PostResponse>>builder()
				.result(postService.getAllPosts())
				.build();
	}
	
	@GetMapping("/{postId}")
	ApiResponse<PostResponse> getPost(@PathVariable String postId){
		return ApiResponse.<PostResponse>builder()
				.result(postService.getPost(postId))
				.build();
	}
	
	@DeleteMapping("/{postId}")
	ApiResponse<?> deletePost(@PathVariable String postId){
		postService.deletePost(postId);
		return ApiResponse.builder()
				.message("Post has been deleted")
				.build();
	}
	
}
