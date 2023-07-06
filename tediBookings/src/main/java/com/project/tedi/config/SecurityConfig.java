package com.project.tedi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authentionProvider;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
        	.csrf()
        	.disable()
        	.authorizeHttpRequests()
        	.requestMatchers("/api/auth/**")
        	.permitAll()
        	.anyRequest()
        	.authenticated()
        	.and()
        	.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	.and()
        	.authenticationProvider(authentionProvider)
        	.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        	
        return httpSecurity.build();
    }
}
