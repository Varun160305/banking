package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.ResponseMsg;
import com.project.dto.UserDto;
import com.project.enums.Role;
import com.project.enums.UserStatus;
import com.project.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody UserDto userDto) {

	    //  Null-safe + enum comparison
	    if (userDto.getRole() != Role.ACCOUNTHOLDER) {
	        return ResponseEntity
	                .badRequest()
	                .body(new ResponseMsg("You can only register as ACCOUNTHOLDER"));
	    }

	    try {
	        UserDto savedUser = userService.createUser(userDto);

	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(new ResponseMsg("User created successfully"));

	    } catch (Exception e) {
	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(new ResponseMsg(e.getMessage()));
	    }
	}
                               	
	@PostMapping("/create-staff")
	public ResponseEntity<?> createStaff(@RequestBody UserDto userDto) {

	    List<Role> allowedRoles = List.of(
	            Role.ADMIN,
	            Role.RELATIONSHIPMANAGER,
	            Role.LOANOFFICER,
	            Role.OPERATIONS,
	            Role.COMPLIANCE
	    );

	    if (!allowedRoles.contains(userDto.getRole())) {
	        return ResponseEntity
	                .badRequest()
	                .body(new ResponseMsg("Invalid staff role"));
	    }

	    try {
	        UserDto savedUser = userService.createUser(userDto);

	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(new ResponseMsg("Staff user created successfully"));

	    } catch (Exception e) {
	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(new ResponseMsg(e.getMessage()));
	    }
	}

	@GetMapping
	public ResponseEntity<List<UserDto>> getAll() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(userService.getUserById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestParam UserStatus status) {
		try {
			return ResponseEntity.ok(userService.updateUserStatus(id, status));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/lock")
	public ResponseEntity<?> lock(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(userService.lockUser(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/unlock")
	public ResponseEntity<?> unlock(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(userService.unlockUser(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
