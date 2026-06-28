package com.project.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.project.dto.FixedDepositDto;
import com.project.dto.ResponseMsg;
import com.project.service.FixedDepositService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/fixed-deposits")
@Slf4j
public class FixedDepositController {

	@Autowired
	FixedDepositService service;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody FixedDepositDto dto) {
		try {
			return new ResponseEntity<>(service.createFD(dto), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(params = "customerId")
	public ResponseEntity<List<FixedDepositDto>> byCustomer(@RequestParam Integer customerId) {
		return ResponseEntity.ok(service.getByCustomer(customerId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.getById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/mature")
	public ResponseEntity<?> mature(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.matureFD(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping("/{id}/close-premature")
	public ResponseEntity<?> closePremature(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.closeFD(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/maturing")
	public ResponseEntity<List<FixedDepositDto>> maturing(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate before) {
		return ResponseEntity.ok(service.getMaturing(before));
	}
}
