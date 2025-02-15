package com.springboot.identity_service.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	private final String[] PUBLIC_ENDPOINT = {"/users", "/auth/token", "/auth/introspect", "/auth/logout" }; 
	
	@Value("${jwt.signerKey}")
	private String signKey;
	
	@Autowired
	private CustomJwtDecoder customJwtDecoder;

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
        			jwtConfigurer.decoder(customJwtDecoder)
        				.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        			.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        		);
        
        httpSecurity.csrf(AbstractHttpConfigurer::disable);	//lambda
        
        return httpSecurity.build();
    }
    /**
     * This bean to change role "SCOPE_" to ""
     * @return
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter () {
    	JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    	jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
    	
    	JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    	jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    	return jwtAuthenticationConverter;
    }
    
    
    @Bean
    PasswordEncoder passwordEncoder () {
    	return new BCryptPasswordEncoder(10);
    }

}
