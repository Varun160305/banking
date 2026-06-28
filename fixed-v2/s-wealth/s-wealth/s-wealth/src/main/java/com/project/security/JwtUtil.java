package com.project.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private String SECRET = "mysecretkeyisvarunduraisamyaginmyusecretkeyisvarunduraisamy";

	public String generateToken(String email, String role) {

		return Jwts.builder() // create the build object
				.setSubject(email)
				.claim("roles", role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.compact();

	}

	public String extractEmail(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();// return email

	}

	public String extractRole(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token) // it is gonna split into 3 parts header(contains algo) payload(actual info of the
										// token based on username or email role,issuedate,exp date)
				// signature(final stage of token)
				.getBody()// it return type is payload
				.get("roles", String.class);
	}

	public boolean validateToken(String token) {
		try {
			System.out.println("validating");
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			// parser - reads the jwt token
			// setsigninkey - methods checks if the token was created using same secret key or not
			// parseClaimsJws - it will check signature, exp date and if it is not valid then it will throw the exception
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
