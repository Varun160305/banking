package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.LoanAccountDto;
import com.project.dto.ResponseMsg;
import com.project.service.LoanAccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/loan-accounts")
@Slf4j
public class LoanAccountController {

	@Autowired
	LoanAccountService service;

	@PostMapping("/disburse")
	public ResponseEntity<?> disburse(@RequestParam Integer applicationId) {
		try {
			return new ResponseEntity<>(service.disburseLoan(applicationId), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(params = "customerId")
	public ResponseEntity<List<LoanAccountDto>> byCustomer(@RequestParam Integer customerId) {
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

	@PatchMapping("/{id}/mark-npa")
	public ResponseEntity<?> markNpa(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.markNPA(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/close")
	public ResponseEntity<?> close(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.closeLoan(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
