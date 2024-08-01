package com.apimobilestore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apimobilestore.dto.request.CommentRequest;
import com.apimobilestore.dto.request.PostRequest;
import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.CommentResponse;
import com.apimobilestore.dto.response.PostResponse;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.entity.Comment;
import com.apimobilestore.entity.Post;
import com.apimobilestore.entity.User;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.mapper.CommentMapper;
import com.apimobilestore.mapper.PostMapper;
import com.apimobilestore.mapper.UserMapper;
import com.apimobilestore.repository.CommentRepository;
import com.apimobilestore.repository.PostRepository;
import com.apimobilestore.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

public interface AdminService {
	UserResponse getInfo(String username);
	List<UserResponse> getAllUser();
	void deleteUser(String username);
	UserResponse updateUser(String username,UserUpdate request);
	
	PostResponse createPost(String username,PostRequest postRequest);
	PostResponse updatePost(String id,PostRequest postRequest);
	void deletePost(String id);
	
	CommentResponse createComment(String userId,String postId,CommentRequest commentRequest);
	CommentResponse updateComment(String id, CommentRequest commentRequest);
	void deleteComment(String id);
}
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

class AdminServiceImpl implements AdminService{
	UserRepository userRepository;
	UserMapper mapper;
	PasswordEncoder passwordEncoder;
	PostRepository postRepository;
	PostMapper postMapper;
	CommentRepository commentRepository;
	CommentMapper commentMapper;
	
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('ADMIN')")
	public UserResponse getInfo(String username) {
		return mapper.toUserResponse(userRepository.findByUsername(username)
				.orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED)));
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserResponse> getAllUser() {
		List<User> users = userRepository.findAll();
	    return users.stream()
	                .map(mapper::toUserResponse)
	                .collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUser(String username) {
		userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        userRepository.deleteByUsername(username);
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public UserResponse updateUser(String username,UserUpdate request) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
		mapper.updateUser(user, request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		return mapper.toUserResponse(userRepository.save(user));
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public PostResponse createPost(String username, PostRequest postRequest) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		Post post = postMapper.toPost(postRequest);
		post.setAuthor(user);
		return postMapper.toPostResponse(postRepository.save(post));
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public PostResponse updatePost(String id, PostRequest postRequest) {
		Post post = postRepository.findById(id).orElseThrow();
        post.setContent(postRequest.getContent());
        post.setTitle(postRequest.getTitle());
        return postMapper.toPostResponse(postRepository.save(post));
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public void deletePost(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        postRepository.delete(post);
		
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public CommentResponse createComment(String username,String postId, CommentRequest commentRequest) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		Comment comment = commentMapper.toComment(commentRequest);
		comment.setAuthor(user);
		comment.setPost(post);
		return commentMapper.toResponse(commentRepository.save(comment));
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public CommentResponse updateComment(String id, CommentRequest commentRequest) {
		Comment comment = commentRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        comment.setContent(commentRequest.getContent());
		return commentMapper.toResponse(commentRepository.save(comment));
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteComment(String id) {
		Comment comment = commentRepository.findById(id)
	            .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));  
	    commentRepository.delete(comment);
		
	}
}
