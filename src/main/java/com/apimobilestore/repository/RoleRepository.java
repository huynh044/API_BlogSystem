package com.apimobilestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apimobilestore.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

}
