package com.pizza.project.dtos;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
@ApiModel(description = "Pizza payload with fields for the pizza resource for update.")
public class PizzaUpdateRequest {
	@ApiModelProperty(notes = "Name of the pizza.", example = "Capricozza Big",  position = 1)
	@NotBlank(message = "Name is mandatory.")
	private String name;
	@ApiModelProperty(notes = "Unique identifier of the pizza.", example = "capricozzam",  position = 2)
	@NotBlank(message = "Slug is mandatory.")
	@Pattern(message = "Only lowercase letters are allowed.", regexp = "[a-z]+")	
	private String slug;
	@ApiModelProperty(notes = "Size of the pizza.", example = "20",  position = 3)
	@NotNull(message = "Size is mandatory.")
	private Long size;
	@ApiModelProperty(notes = "Price of the pizza.", example = "0.5",  position = 4)
	@NotNull(message = "Price is mandatory.")
	@DecimalMin(value = "0.01", message = "Min price is 0.01.")
	private Double price;
}
