package br.com.kuntzeprojects.webscraping.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UnauthorizedException() {
		super();
	}
	
	public UnauthorizedException(String msg) {
		super(msg);
	}
	
	public UnauthorizedException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
