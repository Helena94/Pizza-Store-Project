package com.pizza.project.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.pizza.project.repositories.SecureUserRepository;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

	@Autowired
	private SecureUserRepository userRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		return !userRepository.findByEmail(value).isPresent();
	}

}
