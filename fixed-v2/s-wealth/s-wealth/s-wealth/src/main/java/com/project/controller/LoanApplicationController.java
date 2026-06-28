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

import com.project.dto.LoanApplicationDto;
import com.project.dto.ResponseMsg;
import com.project.enums.LoanApplicationStatus;
import com.project.service.LoanApplicationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/loan-applications")
@Slf4j
public class LoanApplicationController {

	@Autowired
	LoanApplicationService service;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody LoanApplicationDto dto) {
		try {
			return new ResponseEntity<>(service.submitApplication(dto), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(params = "customerId")
	public ResponseEntity<List<LoanApplicationDto>> byCustomer(@RequestParam Integer customerId) {
		return ResponseEntity.ok(service.getByCustomer(customerId));
	}

	@GetMapping(params = "status")
	public ResponseEntity<List<LoanApplicationDto>> byStatus(@RequestParam LoanApplicationStatus status) {
		return ResponseEntity.ok(service.getByStatus(status));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.getById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/under-review")
	public ResponseEntity<?> underReview(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.underReview(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/approve")
	public ResponseEntity<?> approve(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.approveApplication(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/reject")
	public ResponseEntity<?> reject(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.rejectApplication(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
