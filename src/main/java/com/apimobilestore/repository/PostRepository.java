package com.apimobilestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apimobilestore.entity.Post;

public interface PostRepository extends JpaRepository<Post, String> {

}
