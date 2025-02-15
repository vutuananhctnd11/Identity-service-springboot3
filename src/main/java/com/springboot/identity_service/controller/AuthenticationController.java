package com.springboot.identity_service.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.springboot.identity_service.dto.request.ApiResponse;
import com.springboot.identity_service.dto.request.AuthenticationRequest;
import com.springboot.identity_service.dto.request.IntrospectRequest;
import com.springboot.identity_service.dto.request.LogoutRequest;
import com.springboot.identity_service.dto.response.AuthenticationResponse;
import com.springboot.identity_service.dto.response.IntrospectResponse;
import com.springboot.identity_service.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
	AuthenticationService authenticationService;

	@PostMapping("/token")
	ApiResponse<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request){
		AuthenticationResponse result = authenticationService.authenticate(request);
		return ApiResponse.<AuthenticationResponse>builder()
				.result(result)
				.build();
	}
	
	
	@PostMapping("/introspect")
	ApiResponse<IntrospectResponse> authenticate (@RequestBody IntrospectRequest request) throws JOSEException, ParseException{
		IntrospectResponse result = authenticationService.introspect(request);
		return ApiResponse.<IntrospectResponse>builder()
				.result(result)
				.build();
	}
	
	@PostMapping("/logout")
	ApiResponse<Void> logout (@RequestBody LogoutRequest request) throws JOSEException, ParseException{
		authenticationService.logout(request);
		return ApiResponse.<Void>builder().build();
	}
	
}
