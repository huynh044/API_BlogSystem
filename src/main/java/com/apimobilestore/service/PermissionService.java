package com.apimobilestore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.apimobilestore.dto.request.PermissionRequest;
import com.apimobilestore.dto.response.PermissionResponse;
import com.apimobilestore.entity.Permission;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.mapper.PermissionMapper;
import com.apimobilestore.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

public interface PermissionService {
	PermissionResponse createPermission(PermissionRequest request);
	List<PermissionResponse> getAllPermissions();
	void deletePermission(String name);
}

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        String permissionName = request.getName().toUpperCase();

        if (permissionRepository.existsById(permissionName)) {
            throw new AppException(ErrorCode.DUPLICATE_PERMISSION);
        }

        Permission permission = permissionMapper.toPermission(request);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(savedPermission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(permissionMapper::toPermissionResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deletePermission(String name) {
        if (!permissionRepository.existsById(name)) {
            throw new AppException(ErrorCode.UNDEFINEPERMISSION);
        }
        permissionRepository.deleteById(name);
    }
}