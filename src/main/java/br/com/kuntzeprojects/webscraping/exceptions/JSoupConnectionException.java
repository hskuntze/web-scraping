package br.com.kuntzeprojects.webscraping.exceptions;

public class JSoupConnectionException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public JSoupConnectionException(String msg) {
		super(msg);
	}
}
