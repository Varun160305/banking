package com.project.controller;

import java.util.List;
import com.project.dto.DepositRequest;
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

import com.project.dto.BankAccountDto;
import com.project.dto.ResponseMsg;
import com.project.service.BankAccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/bank-accounts")
@Slf4j
public class BankAccountController {

	@Autowired
	BankAccountService service;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody BankAccountDto dto) {
		try {
			return new ResponseEntity<>(service.openAccount(dto), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.CONFLICT);
		}
	}



@PostMapping("/deposit")
public BankAccountDto deposit(@RequestBody DepositRequest req) throws Exception {
    return service.deposit(req.getAccountId(), req.getAmount());
}




	@GetMapping(params = "customerId")
	public ResponseEntity<List<BankAccountDto>> byCustomer(@RequestParam Integer customerId) {
		return ResponseEntity.ok(service.getAccountsByCustomer(customerId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.getById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/number/{accountNumber}")
	public ResponseEntity<?> byNumber(@PathVariable String accountNumber) {
		try {
			return ResponseEntity.ok(service.getAccountByNumber(accountNumber));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/close")
	public ResponseEntity<?> close(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.closeAccount(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
