package com.pizza.project.error;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Contains compactly packed errors made requesting resources with complementary status codes.")
public class ErrorDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(notes = "Title of the erros.", example = "Resource not found.", position = 1)
	private String title;
	@ApiModelProperty(notes = "Http status code.", example = "404", position = 2)
	private int status;
	@ApiModelProperty(notes = "Detail of the error.", example = "Resource by identifier 'exampleId' doesn't exist.", position = 3)
	private String detail;
	@ApiModelProperty(notes = "Error time.", example = "1640200294820", position = 4)
	private long timeStamp;
	@ApiModelProperty(notes = "Shows exception that was made.", example = "1640200294820", position = 5)
	private String developerMessage;
	private Map<String, List<ValidationError>> errors = new HashMap<String, List<ValidationError>>();

}
