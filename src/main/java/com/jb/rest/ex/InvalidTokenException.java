package com.jb.rest.ex;

@SuppressWarnings("serial")
public class InvalidTokenException extends Exception {
	public InvalidTokenException(String message) {
		super(message);
	}

}
