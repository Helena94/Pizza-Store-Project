package com.pizza.project.dtos;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ApiModel(description = "Pizza response payload with fields for the pizza resource.")
public class PizzaResponse {
	@ApiModelProperty(notes = "Name of the pizza.", example = "Capricozza Big", position = 1)
	private String name;
	@ApiModelProperty(notes = "Unique identifier of the pizza.", example = "capricozzam", position = 2)
	private String slug;
	@ApiModelProperty(notes = "Size of the pizza.", example = "20", position = 3)
	private Long size;
	@ApiModelProperty(notes = "Price of the pizza.", example = "0.5", position = 4)
	private Double price;
	@ApiModelProperty(notes = "Date of the recent update of the pizza.", example = "13", position = 5)
	private Date date;
}
