package com.pizza.project.error;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Validation error.")
public class ValidationError implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(notes = "Type of the validation.", example = "NotBlank", position = 1)
	private String code;
	@ApiModelProperty(notes = "Message of the validation.", example = "Field is mandatory.", position = 1)
	private String message;
}
