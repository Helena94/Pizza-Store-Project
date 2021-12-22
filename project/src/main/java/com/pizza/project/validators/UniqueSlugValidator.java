package com.pizza.project.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.pizza.project.repositories.PizzaRepository;

public class UniqueSlugValidator implements ConstraintValidator<UniqueSlug, String> {

	@Autowired
	private PizzaRepository pizzaRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		return !pizzaRepository.findBySlug(value).isPresent();
	}

}
