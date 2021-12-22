package com.pizza.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pizza.project.dtos.LoginRequest;
import com.pizza.project.dtos.LoginResponse;
import com.pizza.project.dtos.PizzaResponse;
import com.pizza.project.dtos.SignUpRequest;
import com.pizza.project.dtos.SignUpResponse;
import com.pizza.project.dtos.UserResponse;
import com.pizza.project.error.ErrorDetail;
import com.pizza.project.exception.ModelException;
import com.pizza.project.services.SecureUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")

@Api(tags = "API of users of the pizza resource. Set of endpoints for sign up, login, list all users, find user and delete user.")
public class SecureUserController {
	@Autowired
	private SecureUserService userService;

	@PostMapping(value = "/public/login", produces = { "application/json" }, consumes = { "application/json" })
	@ApiOperation(value = "Login as user to get bearer token for use of apis depending of the roles. Everyone has the access to this endpoint.", httpMethod = "POST", consumes = "application/json", produces = "application/json")

	@ApiResponses(value = { @ApiResponse(code = 201, response = LoginResponse.class, message = "Created."),
			@ApiResponse(code = 400, response = ErrorDetail.class, message = "Invalid credentials."),
			@ApiResponse(code = 401, response = ErrorDetail.class, message = "Login failed."),
			@ApiResponse(code = 500, message = "Internal error.") })

	public ResponseEntity<LoginResponse> login(HttpServletRequest requestHeader, @RequestBody LoginRequest request) {

		LoginResponse loginResponse = userService.login(request.getUserName(), request.getPassword());
		if (loginResponse == null) {
			throw new ModelException("Login failed", HttpStatus.UNAUTHORIZED,
					"Login failed. Possible cause : incorrect username/password.");
		} else {
			return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
		}
	}

	@ApiOperation(value = "Sign up in other to be able to use api. Everyone has the access to this endpoint.", httpMethod = "POST", consumes = "application/json", produces = "application/json")

	@ApiResponses(value = { @ApiResponse(code = 200, response = SignUpResponse.class, message = "Success."),
			@ApiResponse(code = 400, response = ErrorDetail.class, message = "Bad request."),
			@ApiResponse(code = 422, response = ErrorDetail.class, message = "User already exists."),
			@ApiResponse(code = 500, message = "Internal error.") })
	@PostMapping(value = "/public/signup", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<SignUpResponse> signUp(HttpServletRequest requestHeader,
			@Valid @RequestBody SignUpRequest request) {

		SignUpResponse user;
		try {
			user = userService.signUp(request);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (ModelException e) {
			throw e;

		} catch (Exception e) {
			throw e;
		}
	}

	@DeleteMapping(value = "/delete/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(value = "Delete user by username. Only admins have the access to this endpoint.", httpMethod = "DELETE", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, response = Void.class, message = "Success."),
			@ApiResponse(code = 403, response = ErrorDetail.class, message = "No, or invalid access token."),
			@ApiResponse(code = 404, response = ErrorDetail.class, message = "User not found."),
			@ApiResponse(code = 500, message = "Internal error.") })

	public ResponseEntity<Void> deleteUser(
			@ApiParam(name = "slug", value = "Username of the user.", required = true) @PathVariable String username) {
		try {
			userService.removeUser(username);
		} catch (ModelException e) {
			throw e;

		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping(produces = { "application/json" })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(value = "Get all users. Only admins have the access to this endpoint.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, responseContainer = "List", response = UserResponse.class, message = "Success."),
			@ApiResponse(code = 403, response = ErrorDetail.class, message = "No, or invalid access token."),
			@ApiResponse(code = 404, response = ErrorDetail.class, message = "No users found."),
			@ApiResponse(code = 500, message = "Internal error.") })
	public ResponseEntity<List<UserResponse>> getAllUser() {
		try {
			List<UserResponse> users = userService.getAllUser();
			if (users != null)
				return new ResponseEntity<>(users, HttpStatus.OK);
			else
				throw new ModelException("No users in the system.", HttpStatus.NOT_FOUND, "No users found.");
		} catch (ModelException e) {
			throw e;

		} catch (Exception e) {
			throw e;
		}

	}

	@GetMapping(value = "/search/{username}", produces = { "application/json" })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(value = "Get user by username. Only admins have the access to this endpoint.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, response = PizzaResponse.class, message = "Success."),
			@ApiResponse(code = 403, response = ErrorDetail.class, message = "No, or invalid access token."),
			@ApiResponse(code = 404, response = ErrorDetail.class, message = "User not found."),
			@ApiResponse(code = 500, message = "Internal error.") })
	public ResponseEntity<UserResponse> searchUser(
			@ApiParam(name = "slug", value = "Username of the user.", required = true) @PathVariable String username)
			throws RuntimeException {

		UserResponse userResponse = userService.searchUser(username);
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

	@GetMapping("/refreshToken")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(value = "Refresh token. Only admins have the access to this endpoint.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, response = String.class, message = "Success."),
			@ApiResponse(code = 401, message = "Unauthorized."),
			@ApiResponse(code = 404, response = ErrorDetail.class, message = "User not found."), @ApiResponse(code = 500, message = "Internal error.") })
	public String refreshToken(HttpServletRequest req) {
		return userService.refreshToken(req.getRemoteUser());
	}

}
