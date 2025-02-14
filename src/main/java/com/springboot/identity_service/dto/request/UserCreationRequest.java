package com.springboot.identity_service.dto.request;

import java.time.LocalDate;

import com.springboot.identity_service.validator.DobConstraint;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
/**
 * annotation dùng để khởi tạo tự dộng
 * getter, setter, toString, Constructor
 */
@Data					//annotation of Lombok annotation used to auto create getter, setter, toString, Constructor
@Builder				// create builder class of this DTO
@NoArgsConstructor		// create constructor don't has parameter
@AllArgsConstructor		// create constructor with all parameter
@FieldDefaults(level = AccessLevel.PRIVATE)	//every file is private
public class UserCreationRequest {

	@Size(min = 4, message = "USERNAME_INVALID")//validation data
	String username;
	
	@Size(min = 6, message = "PASSWORD_INVALID")
	String password;
	String firstName;
	String lastName;
	@DobConstraint(min = 16, message = "INVALID_DOB")
	LocalDate dob;	
	
}
