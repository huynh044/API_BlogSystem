package com.apimobilestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apimobilestore.entity.InvalidatedToken;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

}
