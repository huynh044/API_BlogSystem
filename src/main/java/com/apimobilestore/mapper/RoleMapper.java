package com.apimobilestore.mapper;

import org.mapstruct.Mapper;

import com.apimobilestore.dto.request.RoleResquest;
import com.apimobilestore.dto.response.RoleResponse;
import com.apimobilestore.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

	Role toRole(RoleResquest resquest);
	RoleResponse toRoleResponse(Role role);
}
