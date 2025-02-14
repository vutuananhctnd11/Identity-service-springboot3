package com.springboot.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.identity_service.dto.request.RoleRequest;
import com.springboot.identity_service.dto.response.RoleResponse;
import com.springboot.identity_service.entity.Permission;
import com.springboot.identity_service.entity.Role;
import com.springboot.identity_service.mapper.RoleMapper;
import com.springboot.identity_service.repository.PermissionRepository;
import com.springboot.identity_service.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
	
	RoleRepository roleRepository;
	
	PermissionRepository permissionRepository;
	
	RoleMapper roleMapper;
	
	public RoleResponse create (RoleRequest request) {
		Role role = roleMapper.toRole(request);
		
		List<Permission> permissions =  permissionRepository.findAllById(request.getPermissions());
		
		role.setPermissions(new HashSet<>(permissions));
		
		roleRepository.save(role);
		return roleMapper.toRoleResponse(role);
	}
	
	public List<RoleResponse> getAll(){
		List<Role> roles = roleRepository.findAll();
		return roles.stream().map(roleMapper::toRoleResponse).toList();
	}
	
	public void delete(String role) {
		roleRepository.deleteById(role);
	}

}
