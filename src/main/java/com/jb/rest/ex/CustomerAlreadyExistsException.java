package com.jb.rest.ex;

@SuppressWarnings("serial")
public class CustomerAlreadyExistsException extends Exception {
	public CustomerAlreadyExistsException(String message) {
		super(message);
	}

}
