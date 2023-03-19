package com.spring.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultExceptions extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ValNotFoundExcemption.class)
	public final ResponseEntity<Object> ErrorMsg() {
		return new ResponseEntity<>("Not Found In DB",HttpStatus.NOT_FOUND);
	}
	
}
