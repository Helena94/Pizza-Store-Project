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
@ApiModel(description = "Login request payload for user authentication needed to get the bearer JWT token to authorize the APIs.")
public class LoginRequest {
	@ApiModelProperty(notes = "Username of the user in the system.", example = "John.Doe", required = true, position = 1)
    private String userName;
    @ApiModelProperty(notes = "Password of the user in the system.", example = "JohnDoe11@", required = true, position = 2)
    private String password;

  
}
