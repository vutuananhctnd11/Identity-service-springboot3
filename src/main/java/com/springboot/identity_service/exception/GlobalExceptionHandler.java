package com.springboot.identity_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springboot.identity_service.dto.request.ApiResponse;
import com.springboot.identity_service.entity.User;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value =  RuntimeException.class)
	ResponseEntity<ApiResponse<User>> handlingRuntimeException (RuntimeException exception){
		ApiResponse<User> apiResponse = new ApiResponse<>();
		apiResponse.setCode(ErrorCode.UNCATEGOIED_EXCEPION.getCode());
		apiResponse.setMessage(ErrorCode.UNCATEGOIED_EXCEPION.getMessage());
		return ResponseEntity.badRequest().body(apiResponse);
	}
	
	@ExceptionHandler(value =  AppException.class)
	ResponseEntity<ApiResponse<User>> handlingAppException (AppException exception){
		ApiResponse<User> apiResponse = new ApiResponse<>();
		ErrorCode errorCode = exception.getErrorCode();
		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(errorCode.getMessage());
		return ResponseEntity.badRequest().body(apiResponse);
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse<User>> handlingMethodArgumentNotValidException (MethodArgumentNotValidException exception){
		String enumKey = exception.getFieldError().getDefaultMessage();
		ErrorCode errorCode = ErrorCode.KEY_INVALID;	//default enum
		try {
			errorCode = ErrorCode.valueOf(enumKey);
		} catch (IllegalArgumentException e) {
			
		}
		ApiResponse<User> apiResponse = new ApiResponse<User>();
		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(errorCode.getMessage());
		
		return ResponseEntity.badRequest().body(apiResponse);
	}
}
