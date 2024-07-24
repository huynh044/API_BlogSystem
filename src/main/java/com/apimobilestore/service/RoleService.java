package com.apimobilestore.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.apimobilestore.dto.request.RoleResquest;
import com.apimobilestore.dto.response.RoleResponse;
import com.apimobilestore.entity.Permission;
import com.apimobilestore.entity.Role;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.mapper.RoleMapper;
import com.apimobilestore.repository.PermissionRepository;
import com.apimobilestore.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

public interface RoleService {
	RoleResponse createResponse(RoleResquest request);
	List<RoleResponse> getAllRole();
	void deleteRole(String name);
}
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class RoleServiceImpl implements RoleService{
	RoleRepository roleRepository;
	PermissionRepository permissionRepository;
	RoleMapper roleMapper;
	@PreAuthorize("hasRole('ADMIN')")
    @Override
    public RoleResponse createResponse(RoleResquest request) {
        // Convert request to Role entity
        Role role = roleMapper.toRole(request);
        role.setName(role.getName().toUpperCase());

        // Retrieve permissions and set them to the role
        Set<Permission> permissions = request.getPermissions().stream()
            .map(permissionRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());

        role.setPermissions(permissions);

        try {
            role = roleRepository.save(role);
            return roleMapper.toRoleResponse(role);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNDEFINEROLE);
        }
    }
	
	

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<RoleResponse> getAllRole() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteRole(String name) {
        try {
            if (roleRepository.existsById(name)) {
                roleRepository.deleteById(name);
            } else {
                throw new AppException(ErrorCode.UNDEFINEROLE);
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNDEFINEROLE);
        }
    }
}
