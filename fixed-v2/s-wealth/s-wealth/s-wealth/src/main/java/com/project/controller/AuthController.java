package com.project.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.AuthLogin;
import com.project.dto.ResponseMsg;
import com.project.entities.User;
import com.project.security.JwtUtil;
import com.project.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

	@Autowired
	UserService userService;

	@Autowired
	JwtUtil jwtUtil;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthLogin authLogin) {
		try {
			User user = userService.authenticate(authLogin);
			String token = jwtUtil.generateToken(user.getEmail(), user.getRole() + "");
			Map<String, Object> body = new LinkedHashMap<>();
			body.put("token", token);
			body.put("role", user.getRole());
			body.put("userId", user.getUserId());
			return new ResponseEntity<>(body, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.UNAUTHORIZED);
		}
	}
}
