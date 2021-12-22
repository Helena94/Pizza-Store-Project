package com.pizza.project.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pizza.project.dtos.PizzaRequest;
import com.pizza.project.dtos.PizzaResponse;
import com.pizza.project.dtos.PizzaUpdateRequest;
import com.pizza.project.error.ErrorDetail;
import com.pizza.project.exception.ModelException;
import com.pizza.project.services.PizzaSevice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/pizzas")
@Api(tags = "API for pizza resource. Set of endpoints for creating, retrieving, updating and deleting of pizzas.")

public class PizzaController {
	@Autowired
	private PizzaSevice service;

	@Cacheable("pizzas")
	@GetMapping(produces = { "application/json" })
	@ApiOperation(value = "Lists all pizzas sorted by date field in descending order. Everyone has the access to this endpoint.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, responseContainer = "List", response = PizzaResponse.class, message = "Success."),
			@ApiResponse(code = 404, response = ErrorDetail.class, message = "Did not find any pizzas."),
			@ApiResponse(code = 500, message = "Internal error.") })
	public ResponseEntity<List<PizzaResponse>> getPizzas() {
		List<PizzaResponse> pizzas = service.findAll();
		if (pizzas != null && !pizzas.isEmpty())
			return new ResponseEntity<>(pizzas, HttpStatus.OK);
		else
			throw new ModelException("Empty menu", HttpStatus.NOT_FOUND, "Menu is empty.");
	}

	@Cacheable("pizza")
	@GetMapping(value = "/{slug}", produces = { "application/json" })
	@ApiOperation(value = "Gets pizza by slug. Only users have the access to this endpoint.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, response = PizzaResponse.class, message = "Success"),
			@ApiResponse(code = 403, response = ErrorDetail.class, message = "No, or invalid access token."),
			@ApiResponse(code = 404, response = ErrorDetail.class, message = "No pizza by specified slug."),
			@ApiResponse(code = 500, message = "Internal error") })
	public ResponseEntity<PizzaResponse> findBySlug(
			@ApiParam(name = "slug", value = "Unique identifier of the pizza.", required = true) @PathVariable("slug") String slug) {
		PizzaResponse pizza = service.findBySlug(slug);

		return new ResponseEntity<>(pizza, HttpStatus.OK);

	}

	@Cacheable("pizzas")
	@GetMapping(value = "/name", produces = { "application/json" })
	@ApiOperation(value = "Gets pizzas by name, or part of the name, or all if name is not specified. Everyone has the access to this endpoint.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, responseContainer = "List", response = PizzaResponse.class, message = "Success"),
			@ApiResponse(code = 404, response = ErrorDetail.class, message = "Did not find any pizzas."),
			@ApiResponse(code = 500, message = "Internal error") })
	public ResponseEntity<List<PizzaResponse>> findByName(
			@ApiParam(name = "name", value = "Name of the pizza.", required = false) @RequestParam(name = "name", required = false) String name) {

		List<PizzaResponse> pizzas = (name != null) ? service.findByName(name) : service.findAll();
		if (pizzas != null && !pizzas.isEmpty())
			return new ResponseEntity<>(pizzas, HttpStatus.OK);
		else
			throw new ModelException("No pizzas found", HttpStatus.NOT_FOUND,
					String.format("There are no pizzas by name %s.", name));

	}

	@CacheEvict(value = { "pizzas", "pizza" }, allEntries = true)
	@PostMapping(consumes = { "application/json" })
	@ApiOperation(value = "Creates pizza. Only users have the access to this endpoint.", httpMethod = "POST", consumes = "application/json")

	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created,", responseHeaders = {
			@ResponseHeader(name = "Location", description = "Gives the URI of the pizza by slug identifier.") }),
			@ApiResponse(code = 400, response = ErrorDetail.class, message = "Bad request."),
			@ApiResponse(code = 403, response = ErrorDetail.class, message = "No, or invalid access token."),
			@ApiResponse(code = 500, message = "Internal error") })

	public ResponseEntity<Void> createMenuRow(@Valid @RequestBody PizzaRequest pizzaDTO) {

		service.save(pizzaDTO);
		URI newPizzaUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{slug}")
				.buildAndExpand(pizzaDTO.getSlug()).toUri();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(newPizzaUri);

		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}

	@CacheEvict(value = { "pizzas", "pizza" }, allEntries = true)
	@PutMapping(value = "/{slug}", consumes = { "application/json" }, produces = { "application/json" })
	@ApiOperation(value = "Updates pizza's fields by slug. Only users have the access to this endpoint.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = PizzaResponse.class, message = "Success", responseHeaders = {
					@ResponseHeader(name = "Location", description = "Gives the URI of the pizza by slug identifier.") }),
			@ApiResponse(code = 400, response = ErrorDetail.class, message = "Bad request."),
			@ApiResponse(code = 403, response = ErrorDetail.class, message = "No, or invalid access token."),
			@ApiResponse(code = 404, response = ErrorDetail.class, message = "No pizza by specified slug."),
			@ApiResponse(code = 409, response = ErrorDetail.class, message = "Slug already used."),
			@ApiResponse(code = 500, message = "Internal error") })
	public ResponseEntity<PizzaResponse> updateMenuRowBySlug(
			@ApiParam(name = "slug", value = "Unique identifier of the pizza.", required = true) @PathVariable("slug") String slug,
			@Valid @RequestBody PizzaUpdateRequest pizzaDTO) {
		PizzaResponse pizza = service.updateBySlug(slug, pizzaDTO);
		URI newPizzaUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{slug}")
				.buildAndExpand(pizza.getSlug()).toUri();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(newPizzaUri);
		return new ResponseEntity<>(pizza, responseHeaders, HttpStatus.OK);

	}

	@CacheEvict(value = { "pizzas", "pizza" }, allEntries = true)
	@DeleteMapping(value = "/{slug}")
	@ApiOperation(value = "Delete pizza by slug. Only users have the access to this endpoint.", httpMethod = "DELETE", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, response = Void.class, message = "Success"),
			@ApiResponse(code = 403, response = ErrorDetail.class, message = "No, or invalid access token."),
			@ApiResponse(code = 404, response = ErrorDetail.class, message = "No pizza by specified slug."),
			@ApiResponse(code = 500, message = "Internal error") })
	public ResponseEntity<Void> deletePizzaBySlug(
			@ApiParam(name = "slug", value = "Unique identifier of the pizza.", required = true) @PathVariable("slug") String slug) {

		service.deleteBySlug(slug);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
