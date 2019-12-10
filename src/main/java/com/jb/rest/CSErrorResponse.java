package com.jb.rest;

import org.springframework.http.HttpStatus;

/**
 * This class was created to rewrite a more readable exception design
 * 
 * @author Solal Arroues
 *
 */
public class CSErrorResponse {

	// Fields
	private HttpStatus status;
	private String message;
	private long timesTamp;

	// Constructor
	private CSErrorResponse(HttpStatus status, String message, long timesTamp) {
		this.status = status;
		this.message = message;
		this.timesTamp = timesTamp;
	}

	public static CSErrorResponse now(HttpStatus status, String message) {
		return new CSErrorResponse(status, message, System.currentTimeMillis());
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimesTamp() {
		return timesTamp;
	}

	public void setTimesTamp(long timesTamp) {
		this.timesTamp = timesTamp;
	}
}
