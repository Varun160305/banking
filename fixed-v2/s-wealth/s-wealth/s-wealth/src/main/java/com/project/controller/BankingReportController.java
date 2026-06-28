package com.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.BankingReportDto;
import com.project.dto.ResponseMsg;
import com.project.enums.ReportScope;
import com.project.service.BankingReportService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/banking-reports")
@Slf4j
public class BankingReportController {

	@Autowired
	BankingReportService service;

	@PostMapping
	public ResponseEntity<?> generate(@RequestParam(required = false) ReportScope scope) {
		try {
			return new ResponseEntity<>(service.generateReport(scope), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(params = "scope")
	public ResponseEntity<List<BankingReportDto>> byScope(@RequestParam ReportScope scope) {
		return ResponseEntity.ok(service.getByScope(scope));
	}

	@GetMapping
	public ResponseEntity<List<BankingReportDto>> all() {
		return ResponseEntity.ok(service.getAllReports());
	}

	@GetMapping("/summary")
	public ResponseEntity<Map<String, Object>> summary() {
		return ResponseEntity.ok(service.getSummary());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.getReportById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
