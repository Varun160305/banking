package com.project.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getRequestURI();

		// public endpoints skip token validation: login and the internal audit-log POST

if (path.equals("/api/auth/login") 
    || path.equals("/api/users/create") 
    || (path.equals("/api/audit-logs") && "POST".equalsIgnoreCase(request.getMethod()))
)
 {
			System.out.println("Im in jwtfilter");
			filterChain.doFilter(request, response);
			return;
		}

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String token = authHeader.substring(7);
		if (!jwUtil.validateToken(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String email = jwUtil.extractEmail(token);
		String role = jwUtil.extractRole(token);

		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority sg = new SimpleGrantedAuthority(
			    role.startsWith("ROLE_") ? role : "ROLE_" + role
			);
		System.out.println("th role..............................."+sg.toString());


//SimpleGrantedAuthority sg = new SimpleGrantedAuthority(
//    role.startsWith("ROLE_") ? role : "ROLE_" + role
//);


		authorities.add(sg);
		System.out.println("authorities: " + authorities);

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
		System.out.println(auth.toString());
		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);

	}
}
