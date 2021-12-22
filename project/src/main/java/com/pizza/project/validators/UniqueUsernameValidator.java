package com.pizza.project.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.pizza.project.repositories.SecureUserRepository;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	@Autowired
	private SecureUserRepository userRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		return !userRepository.findByUsername(value).isPresent();
	}

}
