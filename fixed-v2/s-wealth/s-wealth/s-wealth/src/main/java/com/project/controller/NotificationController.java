package com.project.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.NotificationDto;
import com.project.dto.ResponseMsg;
import com.project.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {

	@Autowired
	NotificationService service;

	@PostMapping
	public ResponseEntity<NotificationDto> create(@RequestBody NotificationDto dto) {
		return new ResponseEntity<>(service.sendNotification(dto), HttpStatus.CREATED);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<NotificationDto>> byUser(@PathVariable Integer userId) {
		return ResponseEntity.ok(service.getByUser(userId));
	}

	@GetMapping("/user/{userId}/unread")
	public ResponseEntity<List<NotificationDto>> unread(@PathVariable Integer userId) {
		return ResponseEntity.ok(service.getUnread(userId));
	}

	@PatchMapping("/{id}/read")
	public ResponseEntity<?> markRead(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.markAsRead(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/user/{userId}/read-all")
	public ResponseEntity<Map<String, Object>> readAll(@PathVariable Integer userId) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("updated", service.markAllAsRead(userId));
		return ResponseEntity.ok(body);
	}

	@GetMapping("/user/{userId}/count")
	public ResponseEntity<Map<String, Object>> count(@PathVariable Integer userId) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("unreadCount", service.getUnreadCount(userId));
		return ResponseEntity.ok(body);
	}
}
