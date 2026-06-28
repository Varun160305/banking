package com.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.project.enums.Role;
import com.project.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(unique = true, nullable = false, length = 150)
	private String email;

	@Column(length = 20)
	private String phone;

	private Integer branchId;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

	private String password;
}
