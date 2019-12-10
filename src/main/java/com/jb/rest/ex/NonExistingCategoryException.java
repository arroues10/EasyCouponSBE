package com.jb.rest.ex;

@SuppressWarnings("serial")
public class NonExistingCategoryException extends Exception {
	public NonExistingCategoryException(String message) {
		super(message);
	}

}
