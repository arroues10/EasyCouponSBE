package com.jb.rest.ex;

@SuppressWarnings("serial")
public class CompanyAlreadyExistsException extends Exception {
	public CompanyAlreadyExistsException(String message) {
		super(message);
	}

}
