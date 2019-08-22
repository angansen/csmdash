package com.csm.dashboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnathorizedException extends RuntimeException {

	/**
	 * @param message
	 */
	public UnathorizedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
