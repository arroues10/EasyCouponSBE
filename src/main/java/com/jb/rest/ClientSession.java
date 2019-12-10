package com.jb.rest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jb.service.Service;

/**
 * This class represents a session when a member connects to the system he
 * receives back a session that will give him access to the compatible service
 * and therefore the function that is allowed to him
 * 
 * @author Solal Arroues
 *
 */
@Component
@Scope("prototype")
public class ClientSession {

	// Fields
	private Service service;
	private long lastAccessedMillis;

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public long getLastAccessedMillis() {
		return lastAccessedMillis;
	}

	/**
	 * Initializes the last access at the current moment
	 */
	public void accessed() {
		this.lastAccessedMillis = System.currentTimeMillis();
	}

}
