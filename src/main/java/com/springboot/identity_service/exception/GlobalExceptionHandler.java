package com.springboot.identity_service.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springboot.identity_service.dto.request.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value =  RuntimeException.class)
	ResponseEntity<ApiResponse<Object>> handlingRuntimeException (RuntimeException exception){
		ApiResponse<Object> apiResponse = new ApiResponse<>();
		apiResponse.setCode(ErrorCode.UNCATEGOIED_EXCEPION.getCode());
		apiResponse.setMessage(ErrorCode.UNCATEGOIED_EXCEPION.getMessage());
		return ResponseEntity.badRequest().body(apiResponse);
	}
	
	@ExceptionHandler(value =  AppException.class)
	ResponseEntity<ApiResponse<Object>> handlingAppException (AppException exception){
		ApiResponse<Object> apiResponse = new ApiResponse<>();
		ErrorCode errorCode = exception.getErrorCode();
		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(errorCode.getMessage());
		return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse<Object>> handlingMethodArgumentNotValidException (MethodArgumentNotValidException exception){
		String enumKey = exception.getFieldError().getDefaultMessage();
		ErrorCode errorCode = ErrorCode.KEY_INVALID;	//default enum
		try {
			errorCode = ErrorCode.valueOf(enumKey);
		} catch (IllegalArgumentException e) {
			
		}
		ApiResponse<Object> apiResponse = new ApiResponse<>();
		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(errorCode.getMessage());
		
		return ResponseEntity.badRequest().body(apiResponse);
	}
	
	@ExceptionHandler(value = AccessDeniedException.class)
	ResponseEntity<ApiResponse<Object>> handlingAccessDeniedException (AccessDeniedException exception){
		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
		return ResponseEntity.status(errorCode.getStatusCode()).body(
				ApiResponse.builder()
					.code(errorCode.getCode())
					.message(errorCode.getMessage())
					.build()
				);
	}
}
