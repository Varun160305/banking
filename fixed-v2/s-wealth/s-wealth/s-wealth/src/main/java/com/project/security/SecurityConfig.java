package com.project.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

	@Autowired
	JwtFilter jwtFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration cfg = new CorsConfiguration();
//		cfg.setAllowedOrigins(List.of("http://localhost:3000"));
//		cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//		cfg.setAllowedHeaders(List.of("*"));
//		cfg.setAllowCredentials(true);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", cfg);
//		return source;
//	}

	@Bean
	public SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						// public endpoints
						.requestMatchers("/api/auth/login").permitAll()
						.requestMatchers("/api/users/create").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/audit-logs").permitAll()
						// Identity & Access Management (admin manages users; audit log views are admin)
						.requestMatchers("/api/users/create-staff").hasRole("ADMIN")
						.requestMatchers("/api/audit-logs/**").hasRole("ADMIN")
						// Customer Account Management
						.requestMatchers("/api/bank-accounts/**", "/api/account-statements/**")
						.hasAnyRole("ACCOUNTHOLDER", "OPERATIONS", "ADMIN")
						// Fund Transfers & Payments
						.requestMatchers("/api/fund-transfers/**", "/api/beneficiaries/**", "/api/scheduled-payments/**")
						.hasAnyRole("ACCOUNTHOLDER", "ADMIN")
						// Loan & Credit Management
						.requestMatchers("/api/loan-applications/**", "/api/loan-accounts/**", "/api/emi-schedules/**")
						.hasAnyRole("ACCOUNTHOLDER", "LOANOFFICER", "ADMIN")
						// Investment & Portfolio Management (holdings are nested under /api/portfolios)
						.requestMatchers("/api/fixed-deposits/**", "/api/portfolios/**")
						.hasAnyRole("ACCOUNTHOLDER", "RELATIONSHIPMANAGER", "ADMIN")
						// KYC & Compliance
						.requestMatchers("/api/kyc-records/**", "/api/aml-flags/**")
						.hasAnyRole("COMPLIANCE", "ADMIN")
						// Analytics & Reporting
						.requestMatchers("/api/banking-reports/**")
						.hasAnyRole("OPERATIONS", "ADMIN")
						// Notifications (any authenticated user)
						.requestMatchers("/api/notifications/**").authenticated()

						.requestMatchers("/api/bank-accounts/**").permitAll()

						.anyRequest().authenticated())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
