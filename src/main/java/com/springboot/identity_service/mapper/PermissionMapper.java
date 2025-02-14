package com.springboot.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.springboot.identity_service.dto.request.PermissionRequest;
import com.springboot.identity_service.dto.request.UserCreationRequest;
import com.springboot.identity_service.dto.request.UserUpdateRequest;
import com.springboot.identity_service.dto.response.PermissionResponse;
import com.springboot.identity_service.dto.response.UserResponse;
import com.springboot.identity_service.entity.Permission;
import com.springboot.identity_service.entity.User;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

	Permission toPermission (PermissionRequest request);
	PermissionResponse toPermissionResponse (Permission permission);
}
