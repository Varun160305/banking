package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.AuditLogDto;
import com.project.service.AuditLogService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/audit-logs")
@Slf4j
public class AuditLogController {

	@Autowired
	AuditLogService auditLogService;

	/** Pageable list with optional ?userId= / ?module= filters. */
	@GetMapping
	public ResponseEntity<?> get(@RequestParam(required = false) Integer userId,
			@RequestParam(required = false) String module, Pageable pageable) {
		if (userId != null) {
			return ResponseEntity.ok(auditLogService.findByUserId(userId));
		}
		if (module != null) {
			return ResponseEntity.ok(auditLogService.findByModule(module));
		}
		return ResponseEntity.ok(auditLogService.getPage(pageable));
	}

	/** Internal endpoint other modules can call to record an audit entry. */
	@PostMapping
	public ResponseEntity<?> create(@RequestBody AuditLogDto dto) {
		auditLogService.logAction(dto.getUserId(), dto.getAction(), dto.getModule(), dto.getIpAddress(), dto.getEntityId());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
