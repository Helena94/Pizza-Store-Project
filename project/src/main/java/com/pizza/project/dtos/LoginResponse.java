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
@ApiModel(description = "Login response payload needed for user authorization.")
public class LoginResponse {
	@ApiModelProperty(notes = "Username of the user in the system.", example = "John.Doe",  position = 1)
    private String userName;
	@ApiModelProperty(notes = "Email of the user in the system.", example = "John.Doe@email.com",  position = 2)
    private String email;
	@ApiModelProperty(notes = "Token used to authorize use of the rest of APIs.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZXN1ayIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImlhdCI6MTYxNzM1MTE1MSwiZXhwIjoxNjE3MzU0NzUxfQ.LPQsdNInetCwlYyvmcyDseJlGZmPrfUygIl7xN6SRNM",  position = 3)
    private String accessToken;

}
