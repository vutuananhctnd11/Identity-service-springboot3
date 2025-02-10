package com.springboot.identity_service.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.springboot.identity_service.enums.Role;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	private final String[] PUBLIC_ENDPOINT = {"/users", "/auth/token", "/auth/introspect"}; 
	
	@Value("${jwt.signerKey}")
	private String signKey;

	/**
	 * This is a spring security configuration Bean that allows users to access public APIs 
	 * and request JWT authentication with private APIs.
	 * 
	 * @param httpSecurity
	 * @return
	 * @throws Exception
	 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> 
        			request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()
        			.anyRequest().authenticated());
        
        httpSecurity.oauth2ResourceServer(oauth2 ->
        		oauth2.jwt(jwtConfigurer -> 
        			jwtConfigurer.decoder(jwtDecoder())
        				.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        			.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        		);
        
        httpSecurity.csrf(AbstractHttpConfigurer::disable);	//lambda
        
        return httpSecurity.build();
    }
    /**
     * This bean to change role "SCOPE_" to "ROLE_"
     * @return
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter () {
    	JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    	jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    	
    	JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    	jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    	return jwtAuthenticationConverter;
    }
    
    /**
     * This Bean to decode JWT
     * @return
     */
    @Bean
    JwtDecoder jwtDecoder () {
    	
    	SecretKeySpec secretKeySpec =  new SecretKeySpec(signKey.getBytes(), "HS512");
    	
    	NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    	return nimbusJwtDecoder;
    }
    
    
    @Bean
    PasswordEncoder passwordEncoder () {
    	return new BCryptPasswordEncoder(10);
    }

}
