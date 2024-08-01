package com.apimobilestore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apimobilestore.dto.request.RoleResquest;
import com.apimobilestore.dto.request.UpdateRoleRequest;
import com.apimobilestore.dto.response.RoleResponse;
import com.apimobilestore.entity.Role;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.mapper.RoleMapper;
import com.apimobilestore.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

public interface RoleService {
	RoleResponse createResponse(RoleResquest request);
	List<RoleResponse> getAllRole();
	void deleteRole(String name);
	RoleResponse updateResponse(UpdateRoleRequest request);
}
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class RoleServiceImpl implements RoleService{
	RoleRepository roleRepository;
	RoleMapper roleMapper;
	
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
    @Override
    public RoleResponse createResponse(RoleResquest request) {
		 // Convert request to Role entity
	    Role role = roleMapper.toRole(request);
	    role.setName(role.getName().toUpperCase());
	    try {
	        role = roleRepository.save(role); 
	    } catch (DataIntegrityViolationException e) {
	        throw new AppException(ErrorCode.DUPLICATE_ROLE);
	    }
	    return roleMapper.toRoleResponse(role);
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
    @Transactional
    @Override
    public void deleteRole(String name) {
        if (roleRepository.existsById(name)) {
            try {
                roleRepository.deleteById(name);
            } catch (Exception e) {
                throw new AppException(ErrorCode.UNDEFINEROLE);
            }
        } else {
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }
    }



    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
	@Override
	public RoleResponse updateResponse(UpdateRoleRequest request) {
		Role role = roleRepository.findById(request.getName())
	            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

	    // Lưu vai trò đã được cập nhật vào cơ sở dữ liệu
	    role = roleRepository.save(role);

	    // Chuyển đổi thực thể vai trò thành đối tượng phản hồi
	    return roleMapper.toRoleResponse(role);
	}
}
