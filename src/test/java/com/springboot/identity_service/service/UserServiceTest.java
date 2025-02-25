package com.springboot.identity_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.springboot.identity_service.dto.request.UserCreationRequest;
import com.springboot.identity_service.dto.response.UserResponse;
import com.springboot.identity_service.entity.User;
import com.springboot.identity_service.exception.AppException;
import com.springboot.identity_service.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserServiceTest {

	
	@Autowired
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	private UserCreationRequest request;
	private UserResponse userResponse;
	private User user;
	
	
	@BeforeEach
	private void initData() {
		LocalDate dob = LocalDate.of(1990, 03, 02);
		request = UserCreationRequest.builder()
				.username("john")
				.firstName("John")
				.lastName("Doe")
				.password("12345678")
				.dob(dob)
				.build();
		userResponse = UserResponse.builder()
				.id("hfskjdfsdf")
				.username("john")
				.firstName("John")
				.lastName("Doe")
				.dob(dob)		
				.build();
		user = User.builder()
				.id("hfskjdfsdf")
				.username("john")
				.firstName("John")
				.lastName("Doe")
				.dob(dob)	
				.build();
	}
	
	@Test
	public void createUser_validRequest_success () {
		//given
		Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);
		Mockito.when(userRepository.save(any())).thenReturn(user);
		
		//when
		UserResponse response = userService.createRequest(request);
		
		//then
		Assertions.assertThat(response.getId()).isEqualTo("hfskjdfsdf");
		Assertions.assertThat(response.getUsername()).isEqualTo("john");
		
	}
	
	@Test
	public void createUser_userExists_fail () {
		//given
		Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);
		
		//when
		AppException exception =  assertThrows(AppException.class, () -> userService.createRequest(request));
		
		assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
		assertThat(exception.getErrorCode().getMessage()).isEqualTo("User exists");
		
	}
	
	@Test
	@WithMockUser(username = "john")
	public void getMyInfo_valid_success() {
		Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		
		UserResponse response = userService.getMyInfo();
		
		assertThat(response.getUsername()).isEqualTo("john");
		assertThat(response.getId()).isEqualTo("hfskjdfsdf");
	}
	
	@Test
	@WithMockUser(username = "john")
	public void getMyInfo_userNotFound_fail() {
		Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));
		
		//when
		AppException exception =  assertThrows(AppException.class, () -> userService.getMyInfo());
		
		assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
		
	}
}
