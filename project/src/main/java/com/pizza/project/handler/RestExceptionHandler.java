package com.pizza.project.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pizza.project.error.ErrorDetail;
import com.pizza.project.error.ValidationError;
import com.pizza.project.exception.ModelException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ResponseEntity<String> handleControllerException(Throwable ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		if (ex instanceof AccessDeniedException) {
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<>("Access is denied", status);
		}

		return new ResponseEntity<>(ex.getMessage(), status);
	}

	@ExceptionHandler(ModelException.class)
	public ResponseEntity<?> handleModelException(ModelException rnfe, HttpServletRequest request) {

		return new ResponseEntity<>(map(rnfe), null, rnfe.getHttpStatus());
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manve,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorDetail errorDetail = new ErrorDetail();
		// Populate errorDetail instance
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
		errorDetail.setTitle("Validation Failed");
		errorDetail.setDetail("Input validation failed.");
		errorDetail.setDeveloperMessage(manve.getClass().getName());

		// Create ValidationError instances
		List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
		for (FieldError fe : fieldErrors) {

			List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());
			if (validationErrorList == null) {
				validationErrorList = new ArrayList<ValidationError>();
				errorDetail.getErrors().put(fe.getField(), validationErrorList);
			}
			ValidationError validationError = new ValidationError();
			validationError.setCode(fe.getCode());
			validationError.setMessage(messageSource.getMessage(fe, null));
			validationErrorList.add(validationError);
		}

		return handleExceptionInternal(manve, errorDetail, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(status.value());
		errorDetail.setTitle("Message Not Readable");
		errorDetail.setDetail(ex.getMessage());
		errorDetail.setDeveloperMessage(ex.getClass().getName());

		return handleExceptionInternal(ex, errorDetail, headers, status, request);
	}

	private ErrorDetail map(ModelException exc) {
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTimeStamp(new Date().getTime());
		errorDetail.setStatus(exc.getHttpStatus().value());
		errorDetail.setTitle(exc.getTitle());
		errorDetail.setDetail(exc.getMessage());
		errorDetail.setDeveloperMessage(exc.getClass().getName());
		return errorDetail;
	}
}
