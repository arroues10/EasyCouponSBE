package com.jb.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class was created to allow Spring to manage objects that I did not
 * create myself in the project
 * 
 * @author Solal Arroues
 *
 */
@Configuration
public class RestConfiguration {

	@Bean(name = "tokens")
	public Map<String, ClientSession> tokensMap() {
		return new HashMap<>();
	}
}
