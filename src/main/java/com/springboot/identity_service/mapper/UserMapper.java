package com.springboot.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.springboot.identity_service.dto.request.UserCreationRequest;
import com.springboot.identity_service.dto.request.UserUpdateRequest;
import com.springboot.identity_service.dto.response.UserResponse;
import com.springboot.identity_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
	User toUser (UserCreationRequest request);
	UserResponse toUserResponse (User user);
	void updateUser (@MappingTarget User user, UserUpdateRequest request);
}
