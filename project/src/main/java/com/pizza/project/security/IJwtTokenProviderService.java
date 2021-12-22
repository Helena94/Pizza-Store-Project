package com.pizza.project.security;

import com.pizza.project.entities.Role;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;


public interface IJwtTokenProviderService {
	String createToken(String username, Role roles);

	Authentication validateUserAndGetAuthentication(String token);

	String getUsername(String token);

	String parseToken(HttpServletRequest req);

	boolean validateToken(String token);

}
