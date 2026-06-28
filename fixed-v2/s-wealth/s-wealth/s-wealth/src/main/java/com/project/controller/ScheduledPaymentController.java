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

import com.project.dto.ScheduledPaymentDto;
import com.project.dto.ResponseMsg;
import com.project.service.ScheduledPaymentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/scheduled-payments")
@Slf4j
public class ScheduledPaymentController {

	@Autowired
	ScheduledPaymentService service;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody ScheduledPaymentDto dto) {
		try {
			ScheduledPaymentDto saved = service.createSchedule(dto);
			return new ResponseEntity<>(saved, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(params = "customerId")
	public ResponseEntity<List<ScheduledPaymentDto>> byCustomer(@RequestParam Integer customerId) {
		return ResponseEntity.ok(service.getSchedulesByCustomer(customerId));
	}

	@PatchMapping("/{id}/pause")
	public ResponseEntity<?> pause(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.pauseSchedule(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/cancel")
	public ResponseEntity<?> cancel(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.cancelSchedule(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
		
	}
}
