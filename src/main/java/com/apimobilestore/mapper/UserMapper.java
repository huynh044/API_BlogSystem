package com.apimobilestore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.apimobilestore.dto.request.UserCreation;
import com.apimobilestore.dto.request.UserUpdate;
import com.apimobilestore.dto.response.UserResponse;
import com.apimobilestore.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	@Mapping(target = "uid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
	User toUser(UserCreation userCreation);
	UserResponse toUserResponse(User user);
	
	@Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "roles", ignore = true)
	void updateUser(@MappingTarget User user, UserUpdate userUpdate);
}
