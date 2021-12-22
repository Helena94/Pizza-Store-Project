package com.pizza.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pizza.project.dtos.LoginResponse;
import com.pizza.project.dtos.SignUpRequest;
import com.pizza.project.dtos.SignUpResponse;
import com.pizza.project.dtos.UserResponse;
import com.pizza.project.entities.Role;
import com.pizza.project.entities.User;
import com.pizza.project.exception.ModelException;
import com.pizza.project.repositories.SecureUserRepository;
import com.pizza.project.security.IJwtTokenProviderService;

import java.util.List;

import javax.transaction.Transactional;

@Service

public class SecureUserService {

	@Autowired
	private SecureUserRepository secureUserRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private IJwtTokenProviderService jwtTokenProviderService;
	@Autowired
	private AuthenticationManager authenticationManager;

	public LoginResponse login(String userName, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

			User user = secureUserRepository.findByUsername(userName).get();

			LoginResponse loginResponse = mapToLoginResponse(user);

			return loginResponse;
		} catch (AuthenticationException e) {
			throw new ModelException("Invalid credentials", HttpStatus.BAD_REQUEST,
					"Invalid username/password supplied.");
		}
	}

	public SignUpResponse signUp(SignUpRequest request) {
		if (secureUserRepository.existsByUsername(request.getUserName())) {
			throw new ModelException("User already exists", HttpStatus.UNPROCESSABLE_ENTITY,
					String.format("User by %s already exist ", request.getUserName()));
		}

		User user = map(request);

		secureUserRepository.save(user);

		return mapToSignUpResponse(user);
	}

	@Transactional
	public void removeUser(String userName) {
		if (!secureUserRepository.existsByUsername(userName)) {
			throw new ModelException("User not found", HttpStatus.NOT_FOUND,
					String.format("User by %s doesn't exist ", userName));
		}
		secureUserRepository.deleteByUsername(userName);

	}

	public UserResponse searchUser(String userName) {
		User user = secureUserRepository.findByUsername(userName).orElseThrow(() -> new ModelException("User not found",
				HttpStatus.NOT_FOUND, String.format("User by %s doesn't exist ", userName)));

		return mapToUserResponse(user);
	}

	public List<UserResponse> getAllUser() {
		return secureUserRepository.findAll().stream().map(user->mapToUserResponse(user)).toList();
	}

	public String refreshToken(String userName) {
		return jwtTokenProviderService
				.createToken(userName,
						secureUserRepository
								.findByUsername(userName).orElseThrow(() -> new ModelException("User not found",
										HttpStatus.NOT_FOUND, String.format("User by %s doesn't exist ", userName)))
								.getRole());
	}

	private LoginResponse mapToLoginResponse(User user) {
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setEmail(user.getEmail());
		loginResponse.setUserName(user.getUsername());
		loginResponse.setAccessToken(jwtTokenProviderService.createToken(user.getUsername(), user.getRole()));
		return loginResponse;
	}

	private User map(SignUpRequest request) {
		User user = new User();
		user.setUsername(request.getUserName());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEmail(request.getEmail());
		user.setRole(Role.valueOf(request.getRole()));
		request.setPassword(user.getPassword());
		return user;
	}

	private UserResponse mapToUserResponse(User user) {
		UserResponse userResponse = new UserResponse();
		userResponse.setEmail(user.getEmail());
		userResponse.setUserName(user.getUsername());
		userResponse.setRole(user.getRole().name());
		return userResponse;
	}

	private SignUpResponse mapToSignUpResponse(User user) {
		SignUpResponse signUpResponse = new SignUpResponse();
		signUpResponse.setEmail(user.getEmail());
		signUpResponse.setPassword(user.getPassword());
		signUpResponse.setUsername(user.getUsername());
		signUpResponse.setRole(user.getRole().name());
		return signUpResponse;
	}
}
