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

import com.project.dto.InvestmentHoldingDto;
import com.project.dto.PortfolioDto;
import com.project.dto.ResponseMsg;
import com.project.service.PortfolioService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/portfolios")
@Slf4j
public class PortfolioController {

	@Autowired
	PortfolioService service;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody PortfolioDto dto) {
		try {
			return new ResponseEntity<>(service.createPortfolio(dto), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(params = "customerId")
	public ResponseEntity<List<PortfolioDto>> byCustomer(@RequestParam Integer customerId) {
		return ResponseEntity.ok(service.getPortfolioByCustomer(customerId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.getById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/{id}/holdings")
	public ResponseEntity<?> addHolding(@PathVariable Integer id, @RequestBody InvestmentHoldingDto holding) {
		try {
			return new ResponseEntity<>(service.addHolding(id, holding), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{id}/holdings")
	public ResponseEntity<List<InvestmentHoldingDto>> getHoldings(@PathVariable Integer id) {
		return ResponseEntity.ok(service.getHoldings(id));
	}

	@PatchMapping("/{id}/holdings/{holdingId}/redeem")
	public ResponseEntity<?> redeem(@PathVariable Integer id, @PathVariable Integer holdingId) {
		try {
			return ResponseEntity.ok(service.redeemHolding(holdingId));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
