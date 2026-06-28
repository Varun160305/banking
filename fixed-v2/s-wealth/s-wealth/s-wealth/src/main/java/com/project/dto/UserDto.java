package com.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.enums.Role;
import com.project.enums.UserStatus;

public class UserDto {

	private Integer userId;
	private String name;
	private Role role;
	private String email;
	private String phone;
	private Integer branchId;
	private UserStatus status;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;

	public UserDto() {}

	public UserDto(Integer userId, String name, Role role, String email, String phone, Integer branchId, UserStatus status, String password) {
		this.userId = userId;
		this.name = name;
		this.role = role;
		this.email = email;
		this.phone = phone;
		this.branchId = branchId;
		this.status = status;
		this.password = password;
	}

	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public Role getRole() { return role; }
	public void setRole(Role role) { this.role = role; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	public Integer getBranchId() { return branchId; }
	public void setBranchId(Integer branchId) { this.branchId = branchId; }

	public UserStatus getStatus() { return status; }
	public void setStatus(UserStatus status) { this.status = status; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
}
