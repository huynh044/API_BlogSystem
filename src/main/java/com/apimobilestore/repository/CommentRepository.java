package com.apimobilestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apimobilestore.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {

}
