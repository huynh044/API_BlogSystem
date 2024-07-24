package com.apimobilestore.mapper;

import org.mapstruct.Mapper;

import com.apimobilestore.dto.request.PermissionRequest;
import com.apimobilestore.dto.response.PermissionResponse;
import com.apimobilestore.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
	Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
