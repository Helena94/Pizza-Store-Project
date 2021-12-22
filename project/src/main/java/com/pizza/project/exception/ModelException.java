package com.pizza.project.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;
@Getter
public class ModelException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final HttpStatus httpStatus;
	private final String title;

	public ModelException(String title, HttpStatus httpStatus, String message, Throwable cause) {
		super(message, cause);
		this.title = title;
		this.httpStatus = httpStatus;
	}

	public ModelException(String title, HttpStatus httpStatus, String message) {
		super(message);
		this.title = title;
		this.httpStatus = httpStatus;
	}

}
