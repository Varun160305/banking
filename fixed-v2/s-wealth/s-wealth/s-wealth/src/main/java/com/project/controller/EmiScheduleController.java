package com.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.EmiScheduleDto;
import com.project.dto.ResponseMsg;
import com.project.service.EmiScheduleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/emi-schedules")
@Slf4j
public class EmiScheduleController {

	@Autowired
	EmiScheduleService service;

	@GetMapping(params = "loanAccountId")
	public ResponseEntity<List<EmiScheduleDto>> byLoanAccount(@RequestParam Integer loanAccountId) {
		return ResponseEntity.ok(service.getScheduleByLoanAccount(loanAccountId));
	}

	@GetMapping("/overdue")
	public ResponseEntity<List<EmiScheduleDto>> overdue() {
		return ResponseEntity.ok(service.getOverdueEMIs());
	}

	@PatchMapping("/{id}/pay")
	public ResponseEntity<?> pay(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.payEMI(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/loan-account/{id}/summary")
	public ResponseEntity<?> summary(@PathVariable Integer id) {
		try {
			Map<String, Object> s = service.getSummary(id);
			return ResponseEntity.ok(s);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
