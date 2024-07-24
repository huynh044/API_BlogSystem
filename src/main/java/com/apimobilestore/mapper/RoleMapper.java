package com.apimobilestore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.apimobilestore.dto.request.RoleResquest;
import com.apimobilestore.dto.response.RoleResponse;
import com.apimobilestore.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	@Mapping(target = "permissions", ignore = true)
	Role toRole(RoleResquest resquest);
	RoleResponse toRoleResponse(Role role);
}
