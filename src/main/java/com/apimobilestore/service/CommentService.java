package com.apimobilestore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apimobilestore.dto.request.CommentRequest;
import com.apimobilestore.dto.response.CommentResponse;
import com.apimobilestore.entity.Comment;
import com.apimobilestore.entity.Post;
import com.apimobilestore.entity.User;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.mapper.CommentMapper;
import com.apimobilestore.repository.CommentRepository;
import com.apimobilestore.repository.PostRepository;
import com.apimobilestore.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

public interface CommentService {
	CommentResponse createComment(String postId,CommentRequest commentRequest);
	CommentResponse updateComment(String id, CommentRequest commentRequest);
	List<CommentResponse> getComments(String postId);
	void deleteComment(String id);
}

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
class CommentServiceImpl implements CommentService{
	PostRepository postRepository;
	UserRepository userRepository;
	CommentRepository commentRepository;
	CommentMapper commentMapper;

	@Override
	@PreAuthorize("hasRole('VIEWER')")
	@Transactional
	public CommentResponse createComment(String postId,CommentRequest commentRequest) {
		var context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
		Comment comment = commentMapper.toComment(commentRequest);
		comment.setAuthor(user);
		comment.setPost(post);
		return commentMapper.toResponse(commentRepository.save(comment));
		
		
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('VIEWER')")
	public CommentResponse updateComment(String id, CommentRequest commentRequest) {
		var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Comment comment = commentRepository.findById(id).orElseThrow();
        if(!comment.getAuthor().getUid().equals(user.getUid())) {
        	throw new AppException(ErrorCode.COMMENT_NOT_EXISTED);
        }
        comment.setContent(commentRequest.getContent());
		return commentMapper.toResponse(commentRepository.save(comment));
		
	}

	@Override
	@PreAuthorize("hasRole('VIEWER')")
	public List<CommentResponse> getComments(String postId) {
		List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                       .map(commentMapper::toResponse)
                       .collect(Collectors.toList());
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('VIEWER')")
	public void deleteComment(String id) {
	    var context = SecurityContextHolder.getContext();
	    String username = context.getAuthentication().getName();
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
	    Comment comment = commentRepository.findById(id)
	            .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
	    Post post = comment.getPost();
	    
	    boolean isAuthorOfComment = comment.getAuthor().getUid().equals(user.getUid());
	    boolean isAuthorOfPost = post.getAuthor().getUid().equals(user.getUid());
	    
	    if (!isAuthorOfComment && !isAuthorOfPost) {
	        throw new AppException(ErrorCode.UNAUTHENTICATED);
	    }
	    
	    commentRepository.delete(comment);
	}

}
