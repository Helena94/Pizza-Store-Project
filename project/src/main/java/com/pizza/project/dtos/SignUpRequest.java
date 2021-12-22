package com.pizza.project.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.pizza.project.validators.UniqueEmail;
import com.pizza.project.validators.UniqueUsername;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiModel(description = "Register request payload so newly user can access the system's secure apis.")
public class SignUpRequest {
	@ApiModelProperty(notes = "Username of the user in the system.", example = "John.Doe", required = true, position = 1)
	@Size(min = 4, max = 255, message = "Minimum username length: 4 characters.")
	@NotBlank(message = "Username is mandatory.")
	@UniqueUsername
	private String userName;
	@ApiModelProperty(notes = "Email of the user in the system.", example = "John.Doe@email.com", position = 2)
	@Email(message = "Invalid mail format.")
	@UniqueEmail
	@NotBlank(message = "Email is mandatory.")
	private String email;
	@NotBlank(message = "Password is mandatory.")
	@ApiModelProperty(notes = "Password of the user in the system.", example = "JohnDoe11@", required = true, position = 3)
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Passwor must contain minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character.")
	private String password;
	@NotBlank(message = "This field can be either ROLE_USER or ROLE_ADMIN.")
	@ApiModelProperty(notes = "Role of the user in the system. Tells about access to APIs of the user.", example = "ROLE_USER/ROLE_ADMIN", required = true, position = 3)
	@Pattern(regexp = "ROLE_USER|ROLE_ADMIN", message = "This field can be either ROLE_USER or ROLE_ADMIN.")
	private String role;

}
