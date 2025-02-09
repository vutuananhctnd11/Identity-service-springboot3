package com.springboot.identity_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.identity_service.dto.request.ApiResponse;
import com.springboot.identity_service.dto.request.UserCreationRequest;
import com.springboot.identity_service.dto.request.UserUpdateRequest;
import com.springboot.identity_service.dto.response.UserResponse;
import com.springboot.identity_service.entity.User;
import com.springboot.identity_service.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

	UserService userSrevice;
	
	@PostMapping
	public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
		ApiResponse<User> apiResponse = new ApiResponse<>();
		apiResponse.setResult(userSrevice.createRequest(request));
		return apiResponse;
	}
	
	@GetMapping
	public ApiResponse<List<User>>  getUsers () {
		ApiResponse<List<User>> apiResponse = new ApiResponse<>();
		apiResponse.setResult(userSrevice.getUsers());
		return apiResponse;
	}
	
	@GetMapping("/{userId}")
	public ApiResponse<UserResponse> getUser (@PathVariable String userId) {
		ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
		apiResponse.setResult(userSrevice.getUser(userId));
		return apiResponse;
	}
	
	@PutMapping("/{userId}")
	public UserResponse updateUser (@PathVariable String userId, @RequestBody UserUpdateRequest request) {
		return userSrevice.updateUser(userId, request);
	}
	
	@DeleteMapping("/{userId}")
	public String deleteUser (@PathVariable String userId) {
		userSrevice.deletaUser(userId);
		return "User has been deleted"; 
	}
}
