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
import com.apimobilestore.dto.response.ApiResponse;
import com.apimobilestore.dto.response.CommentResponse;
import com.apimobilestore.service.CommentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
	CommentService commentService;
	
	@PostMapping("/{postId}")
	ApiResponse<CommentResponse> createComment(@PathVariable String postId, @RequestBody CommentRequest request){
		return ApiResponse.<CommentResponse>builder()
				.result(commentService.createComment(postId, request))
				.build();
	}
	
	@PutMapping("/{cmtId}")
	ApiResponse<CommentResponse> updateComment(@PathVariable String cmtId, @RequestBody CommentRequest request){
		return ApiResponse.<CommentResponse>builder()
				.result(commentService.updateComment(cmtId, request))
				.build();
	}
	
	@GetMapping("/{postId}")
	ApiResponse<List<CommentResponse>> getComment(@PathVariable String postId){
		return ApiResponse.<List<CommentResponse>>builder()
				.result(commentService.getComments(postId))
				.build();
	}
	
	@DeleteMapping("/{cmtId}")
	ApiResponse<CommentResponse> deleteComment(@PathVariable String cmtId){
		commentService.deleteComment(cmtId);
		return ApiResponse.<CommentResponse>builder()
				.message("Deleted comment successful")
				.build();
	}
}
