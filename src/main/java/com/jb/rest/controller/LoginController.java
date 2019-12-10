package com.jb.rest.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jb.rest.ClientSession;
import com.jb.rest.LoginSystem;
import com.jb.rest.ex.InvalidLoginException;

@RestController
@RequestMapping("/api")
public class LoginController {

	// Fields
	private static final int LENGTH_TOKEN = 15;

	private LoginSystem loginSystem;
	private Map<String, ClientSession> tokensMap;

	// Constructor
	@Autowired
	public LoginController(LoginSystem loginSystem, @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
		this.loginSystem = loginSystem;
		this.tokensMap = tokensMap;
	}

	/**
	 * This function allows me to connect to the system by email and password using
	 * the login function in loginSystem
	 * 
	 * @param email
	 * @param password
	 * @param loginType
	 * @return ResponseEntity<String> : Token
	 * @throws InvalidLoginException : if the email or the password is invalids
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password,
			@RequestParam String loginType) throws InvalidLoginException {
		ClientSession session = loginSystem.login(email, password, loginType);
		String token = generateToken();

		tokensMap.put(token, session);

		return ResponseEntity.ok(token);
	}

	/**
	 * This function is a private function that generates a 15 digit token and
	 * letter without "-"
	 * 
	 * @return String
	 */
	private String generateToken() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, LENGTH_TOKEN);
	}

}
