package com.project.service;

import java.util.List;

import com.project.dto.AuthLogin;
import com.project.dto.UserDto;
import com.project.entities.User;
import com.project.enums.UserStatus;

public interface UserService {

	UserDto createUser(UserDto dto) throws Exception;

	UserDto getUserById(Integer id) throws Exception;

	List<UserDto> getAllUsers();

	UserDto updateUserStatus(Integer id, UserStatus status) throws Exception;

	UserDto lockUser(Integer id) throws Exception;

	UserDto unlockUser(Integer id) throws Exception;

	/** Validate credentials for login. Returns the user or throws on failure. */
	User authenticate(AuthLogin login) throws Exception;
}
