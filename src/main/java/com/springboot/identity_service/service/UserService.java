package com.springboot.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.identity_service.dto.request.UserCreationRequest;
import com.springboot.identity_service.dto.request.UserUpdateRequest;
import com.springboot.identity_service.dto.response.UserResponse;
import com.springboot.identity_service.entity.Role;
import com.springboot.identity_service.entity.User;
import com.springboot.identity_service.enums.RoleEnums;
import com.springboot.identity_service.exception.AppException;
import com.springboot.identity_service.exception.ErrorCode;
import com.springboot.identity_service.mapper.UserMapper;
import com.springboot.identity_service.repository.RoleRepository;
import com.springboot.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor // create a constructor, after that add all FINAL field to constructor and jnject it to class
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    RoleRepository roleRepository;

    public UserResponse createRequest(UserCreationRequest request) {

//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new AppException(ErrorCode.USER_EXISTS);
//        }
    	
    	
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        
        roles.add(Role.builder().name(RoleEnums.USER.name()).description(RoleEnums.USER.name()).build());

        user.setRoles(roles);
        
        try {
        	user = userRepository.save(user);
        	
        } catch (DataIntegrityViolationException e) {
			throw new AppException(ErrorCode.USER_EXISTS);
		}

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    // @PreAuthorize("hasAuthority('UPDATE_DATA')") // for check permission
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS)));
    }

    public UserResponse getMyInfo() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        List<com.springboot.identity_service.entity.Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deletaUser(String userId) {
        userRepository.deleteById(userId);
    }
}
