package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.AmlFlagDto;
import com.project.dto.ResponseMsg;
import com.project.enums.AmlStatus;
import com.project.service.AmlFlagService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/aml-flags")
@Slf4j
public class AmlFlagController {

	@Autowired
	AmlFlagService service;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody AmlFlagDto dto) {
		try {
			return new ResponseEntity<>(service.raiseFlag(dto), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(params = "accountId")
	public ResponseEntity<List<AmlFlagDto>> byAccount(@RequestParam Integer accountId) {
		return ResponseEntity.ok(service.getFlagsByAccount(accountId));
	}

	@GetMapping(params = "status")
	public ResponseEntity<List<AmlFlagDto>> byStatus(@RequestParam AmlStatus status) {
		return ResponseEntity.ok(service.getByStatus(status));
	}

	@PatchMapping("/{id}/investigate")
	public ResponseEntity<?> investigate(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.investigateFlag(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/clear")
	public ResponseEntity<?> clear(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.clearFlag(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
