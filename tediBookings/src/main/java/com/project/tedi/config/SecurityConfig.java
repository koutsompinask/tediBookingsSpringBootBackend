package com.project.tedi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.tedi.model.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authentionProvider;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
        	.csrf()
        	.disable()
        	.authorizeHttpRequests()
        	.requestMatchers("/api/auth/**",
        			"/v2/api-docs",
                    "/configuration/ui",
                    "/swagger-resources/**",
                    "/configuration/security",
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/error",
                    "/customError",
                    "/access-denied")
            .permitAll()
            .requestMatchers(HttpMethod.GET,"/api/photo/**")
            .permitAll()
            .requestMatchers(HttpMethod.GET,"/api/rating/**")
            .permitAll()
            .requestMatchers(HttpMethod.DELETE,"/api/photo/**")
            .hasAnyAuthority(Role.HOST.name(),Role.HOST_AND_RENTER.name())
            .requestMatchers(HttpMethod.GET,"api/accomodation/**")
            .permitAll()
            .requestMatchers("api/accomodation/getFiltered")
            .permitAll()
            .requestMatchers(HttpMethod.PUT,"api/accomodation/**")
            .hasAnyAuthority(Role.HOST.name(),Role.HOST_AND_RENTER.name())
            .requestMatchers(HttpMethod.POST,"api/accomodation/**")
            .hasAnyAuthority(Role.HOST.name(),Role.HOST_AND_RENTER.name())
            .requestMatchers(HttpMethod.PUT,"api/book/**")
            .hasAnyAuthority(Role.RENTER.name(),Role.HOST_AND_RENTER.name())
            .requestMatchers(HttpMethod.POST,"api/book/**")
            .hasAnyAuthority(Role.RENTER.name(),Role.HOST_AND_RENTER.name())
            .requestMatchers("/api/admin/**")
            .hasAnyAuthority(Role.ADMIN.name())
        	.anyRequest()
        	.authenticated()
        	.and()
        	.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	.and()
        	.authenticationProvider(authentionProvider)
        	.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        	.cors();
        	
        return httpSecurity.build();
    }
}
