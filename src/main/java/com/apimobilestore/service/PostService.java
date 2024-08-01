package com.apimobilestore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apimobilestore.dto.request.PostRequest;
import com.apimobilestore.dto.response.PostResponse;
import com.apimobilestore.entity.Post;
import com.apimobilestore.entity.User;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.mapper.PostMapper;
import com.apimobilestore.repository.PostRepository;
import com.apimobilestore.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

public interface PostService {
	PostResponse createPost(PostRequest postRequest);
	PostResponse updatePost(String id,PostRequest postRequest);
	List<PostResponse> getAllPosts();
	PostResponse getPost(String id);
	void deletePost(String id);
	
}
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class PostServiceImpl implements PostService{
	PostRepository postRepository;
	UserRepository userRepository;
	PostMapper postMapper;
	@Override
	@Transactional
	@PreAuthorize("hasRole('AUTHOR')")
	public PostResponse createPost(PostRequest postRequest) {
		var context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		Post post = postMapper.toPost(postRequest);
		post.setAuthor(user);
		return postMapper.toPostResponse(postRepository.save(post));
	}
	@Override
	@Transactional
	@PreAuthorize("hasRole('AUTHOR')")
	public PostResponse updatePost(String id, PostRequest postRequest) {
		var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Post post = postRepository.findById(id).orElseThrow();
        if (!post.getAuthor().getUid().equals(user.getUid())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        post.setContent(postRequest.getContent());
        post.setTitle(postRequest.getTitle());
        return postMapper.toPostResponse(postRepository.save(post));
	}
	@Override
	@PreAuthorize("hasRole('AUTHOR')")
	public List<PostResponse> getAllPosts() {
		List<Post> posts = postRepository.findAll();
	    return posts.stream()
	                .map(postMapper::toPostResponse)
	                .collect(Collectors.toList());
	}
	@Override
	@PreAuthorize("hasRole('AUTHOR')")
	public PostResponse getPost(String id) {
		Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        return postMapper.toPostResponse(post);
	}
	@Override
	@Transactional
	@PreAuthorize("hasRole('AUTHOR')")
	public void deletePost(String id) {
		var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
		Post post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
		if (!post.getAuthor().getUid().equals(user.getUid())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        postRepository.delete(post);

	}
	
}
