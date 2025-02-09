package com.springboot.identity_service.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.springboot.identity_service.dto.request.AuthenticationRequest;
import com.springboot.identity_service.dto.request.IntrospectRequest;
import com.springboot.identity_service.dto.response.AuthenticationResponse;
import com.springboot.identity_service.dto.response.IntrospectResponse;
import com.springboot.identity_service.exception.AppException;
import com.springboot.identity_service.exception.ErrorCode;
import com.springboot.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

	UserRepository userRepository;
	
	@NonFinal
	@Value("${jwt.signerKey}")	// annotation to extract variables from application.yaml file
	protected String SIGNER_KEY ;
	
	
	public IntrospectResponse introspect (IntrospectRequest request) throws JOSEException, ParseException {
		String token = request.getToken();
		
		
		JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
		
		SignedJWT signedJWT = SignedJWT.parse(token);
		
		Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();
		
		boolean verified =  signedJWT.verify(verifier);
		
		return IntrospectResponse.builder()
				.valid(verified && expityTime.after(new Date()))
				.build();
		
	}
	
	
	public AuthenticationResponse authenticate (AuthenticationRequest request) {
		var user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTS));
		
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		boolean authenticated =  passwordEncoder.matches(request.getPassword(), user.getPassword());
		
		if(!authenticated) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}
		String token = generateToken(request.getUsername());
		
		return AuthenticationResponse.builder()
				.token(token).authenticated(true)
				.build();
	}
	
	private String generateToken (String username) {
		
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
		
		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(username)
				.issuer("TuananhJavaDev")
				.issueTime(new Date())
				.expirationTime(new Date(Instant.now().plus(1,ChronoUnit.HOURS).toEpochMilli())) //expires after 1 hour
				.claim("customClaim", "Custom") 	//optional
				.build();
		
		Payload payload = new Payload(jwtClaimsSet.toJSONObject());
			
		JWSObject jwsObject = new JWSObject(header,payload);
		
		try {
			jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {
			log.error("cann't create token", e);
			throw new  RuntimeException(e);
		}
		
		
	}
}
