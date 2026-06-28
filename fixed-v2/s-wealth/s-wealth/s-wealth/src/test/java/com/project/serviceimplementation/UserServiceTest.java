										package com.project.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.dto.UserDto;
import com.project.entities.User;
import com.project.enums.Role;
import com.project.enums.UserStatus;
import com.project.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	UserRepository repo;

	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	UserServiceImplementation service;

	@Test
	void createUser_valid() throws Exception {
		when(repo.existsByEmail("new@bank.com")).thenReturn(false);
		when(passwordEncoder.encode(anyString())).thenReturn("$2a$hashed");
		when(repo.save(any(User.class))).thenAnswer(inv -> {
			User u = inv.getArgument(0);
			if (u.getUserId() == null) {
				u.setUserId(10);
			}
			return u;
		});

		UserDto dto = new UserDto();
		dto.setName("New User");
		dto.setEmail("new@bank.com");
		dto.setRole(Role.ACCOUNTHOLDER);
		dto.setPassword("secret123");

		UserDto result = service.createUser(dto);

		assertNotNull(result.getUserId());
		assertEquals(UserStatus.Active, result.getStatus());
	}

	@Test
	void createUser_duplicateEmail_throwsException() {
		when(repo.existsByEmail("dup@bank.com")).thenReturn(true);
		UserDto dto = new UserDto();
		dto.setEmail("dup@bank.com");
		assertThrows(Exception.class, () -> service.createUser(dto));
	}

	@Test
	void getUserById_invalid_throwsException() {
		when(repo.findById(999)).thenReturn(Optional.empty());
		assertThrows(Exception.class, () -> service.getUserById(999));
	}

	@Test
	void lockUser_setsLocked() throws Exception {
		User u = User.builder().userId(1).email("a@bank.com").status(UserStatus.Active).build();
		when(repo.findById(1)).thenReturn(Optional.of(u));
		when(repo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

		UserDto result = service.lockUser(1);

		assertEquals(UserStatus.Locked, result.getStatus());
	}

	@Test
	void unlockUser_setsActive() throws Exception {
		User u = User.builder().userId(1).email("a@bank.com").status(UserStatus.Locked).build();
		when(repo.findById(1)).thenReturn(Optional.of(u));
		when(repo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

		UserDto result = service.unlockUser(1);

		assertEquals(UserStatus.Active, result.getStatus());
	}
}
