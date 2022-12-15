package com.self.eventtracking.eventservice.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// All requests must be authenticated
		http.authorizeHttpRequests(
			auth -> auth.anyRequest().authenticated()
		);
		
		// if a request is not authenticated, a web page is shown
		// actually it will show a alert box kind of popup asking creds
		http.httpBasic(withDefaults());
		
		// disable csrf
		http.csrf().disable();
		
		return http.build();
	}
}