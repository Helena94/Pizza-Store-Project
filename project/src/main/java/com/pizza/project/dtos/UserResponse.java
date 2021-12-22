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
@ApiModel(description = "User response payload with fields of the user resource.")
public class UserResponse {
	@ApiModelProperty(notes = "Username of the user in the system.", example = "John.Doe", required = true, position = 1)
	private String userName;
	@ApiModelProperty(notes = "Email of the user in the system.", example = "John.Doe@email.com", position = 2)
	private String email;
	@ApiModelProperty(notes = "Role of the user in the system.", example = "ROLE_USER/ROLE_ADMIN", required = true, position = 4)
	private String role;
}
