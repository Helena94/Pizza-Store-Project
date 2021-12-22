package com.pizza.project.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface UniqueUsername {

	String message() default "This username already exists.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
