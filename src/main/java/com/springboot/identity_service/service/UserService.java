package com.springboot.identity_service.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.identity_service.dto.request.UserCreationRequest;
import com.springboot.identity_service.dto.request.UserUpdateRequest;
import com.springboot.identity_service.dto.response.UserResponse;
import com.springboot.identity_service.entity.User;
import com.springboot.identity_service.exception.AppException;
import com.springboot.identity_service.exception.ErrorCode;
import com.springboot.identity_service.mapper.UserMapper;
import com.springboot.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor		//create a constructor, after that add all FINAL field to constructor and jnject it to class
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

	UserRepository userRepository;
	
	UserMapper userMapper;
	
	public User createRequest (UserCreationRequest request) {
		
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new AppException(ErrorCode.USER_EXISTS);
		}
		User user = userMapper.toUser(request);
		
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		user.setPassword(passwordEncoder.encode(user.getPassword()));		
		return userRepository.save(user);
	}
	
	public List<User> getUsers (){
		return userRepository.findAll();
	}
	
	public UserResponse getUser (String id) {
		return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS)));
	}
	
	public UserResponse updateUser (String UserId, UserUpdateRequest request) {
		User user = userRepository.findById(UserId)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
		userMapper.updateUser(user, request);
		
		return userMapper.toUserResponse(userRepository.save(user));
	}
	
	public void deletaUser (String userId) {
		userRepository.deleteById(userId);
	}
}
