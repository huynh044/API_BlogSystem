package com.apimobilestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apimobilestore.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {
	List<Comment> findByPostId(String postId);
}
