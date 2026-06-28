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

import com.project.dto.KycRecordDto;
import com.project.dto.ResponseMsg;
import com.project.service.KycRecordService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/kyc-records")
@Slf4j
public class KycRecordController {

	@Autowired
	KycRecordService service;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody KycRecordDto dto) {
		try {
			return new ResponseEntity<>(service.submitKYC(dto), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(params = "customerId")
	public ResponseEntity<List<KycRecordDto>> byCustomer(@RequestParam Integer customerId) {
		return ResponseEntity.ok(service.getKYCByCustomer(customerId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.getById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/verify")
	public ResponseEntity<?> verify(@PathVariable Integer id, @RequestParam Integer verifiedBy) {
		try {
			return ResponseEntity.ok(service.verifyKYC(id, verifiedBy));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/reject")
	public ResponseEntity<?> reject(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.rejectKYC(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
