package com.apimobilestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apimobilestore.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {

}
