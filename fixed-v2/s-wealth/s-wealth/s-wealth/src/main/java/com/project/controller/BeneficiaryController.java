package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.BeneficiaryDto;
import com.project.dto.ResponseMsg;
import com.project.service.BeneficiaryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/beneficiaries")
@Slf4j
public class BeneficiaryController {

	@Autowired
	BeneficiaryService service;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody BeneficiaryDto dto) {
		try {
			BeneficiaryDto saved = service.addBeneficiary(dto);
			return new ResponseEntity<>(saved, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	@GetMapping(params = "customerId")
	public ResponseEntity<List<BeneficiaryDto>> byCustomer(@RequestParam Integer customerId) {
		return ResponseEntity.ok(service.getBeneficiariesByCustomer(customerId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.getById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		try {
			service.deleteBeneficiary(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
