package com.springboot.identity_service.exception;

public enum ErrorCode {

	KEY_INVALID(1001, "Invalid message key"),
	USER_EXISTS(1002, "User exists"),
	UNCATEGOIED_EXCEPION(9999, "Uncategorized Exception"),
	USERNAME_INVALID(1003, "Username must be at least 3 character"),
	PASSWORD_INVALID(1004,"Password must be at least 8 character"),
	USER_NOT_EXISTS(1005, "User not exists"),
	UNAUTHENTICATED(1006, "Unauthenticated"),
	;
	
	
	
	private ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	private int code;
	private String message;
	
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
	
	
}
