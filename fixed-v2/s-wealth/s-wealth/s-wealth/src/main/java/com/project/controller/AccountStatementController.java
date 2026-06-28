package com.project.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.AccountStatementDto;
import com.project.dto.ResponseMsg;
import com.project.service.AccountStatementService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/account-statements")
@Slf4j
public class AccountStatementController {

	@Autowired
	AccountStatementService service;

	@PostMapping
	public ResponseEntity<?> generate(@RequestParam Integer accountId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		try {
			return new ResponseEntity<>(service.generateStatement(accountId, from, to), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(params = "accountId")
	public ResponseEntity<List<AccountStatementDto>> byAccount(@RequestParam Integer accountId) {
		return ResponseEntity.ok(service.getStatementsByAccount(accountId));
	}
}
