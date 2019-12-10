package com.jb.rest.ex;

@SuppressWarnings("serial")
public class InvalidLoginException extends Exception {
	public InvalidLoginException(String message) {
		super(message);
	}
}
