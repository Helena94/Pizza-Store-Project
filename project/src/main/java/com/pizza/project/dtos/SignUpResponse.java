package com.pizza.project.dtos;

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
@ApiModel(description = "Sing up response payload returned when user is registered.")
public class SignUpResponse {
	@ApiModelProperty(notes = "Username of the user in the system.", example = "John.Doe", required = true, position = 1)
	private String username;
	@ApiModelProperty(notes = "Email of the user in the system.", example = "John.Doe@email.com", position = 2)
	private String email;
	@ApiModelProperty(notes = "Password of the user in the system.", example = "JohnDoe11@", required = true, position = 3)
	private String password;
	@ApiModelProperty(notes = "Role of the user in the system.", example = "ROLE_USER/ROLE_ADMIN", required = true, position = 4)
	private String role;

}
