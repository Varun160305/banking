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

import com.project.dto.FundTransferDto;
import com.project.dto.ResponseMsg;
import com.project.service.FundTransferService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/fund-transfers")
@Slf4j
public class FundTransferController {

	@Autowired
	FundTransferService service;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody FundTransferDto dto) {
		try {
			if (dto.getFromAccountId() != null && dto.getFromAccountId().equals(dto.getToAccountId())) {
				return new ResponseEntity<>(new ResponseMsg("Source and destination accounts must be different."),
						HttpStatus.BAD_REQUEST);
			}
			FundTransferDto saved = service.initiateTransfer(dto.getFromAccountId(), dto.getToAccountId(),
					dto.getAmount(), dto.getTransferType(),dto.getRemarks());
			return new ResponseEntity<>(saved, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(params = "fromAccountId")
	public ResponseEntity<?> byFrom(@RequestParam Integer fromAccountId) {
	    try {

	        if (fromAccountId == null) {
	            return new ResponseEntity<>(
	                    new ResponseMsg("fromAccountId is required"),
	                    HttpStatus.BAD_REQUEST
	            );
	        }

	        List<FundTransferDto> result = service.getByFromAccountId(fromAccountId);

	        return new ResponseEntity<>(result, HttpStatus.OK);

	    } catch (Exception e) {
	        return new ResponseEntity<>(
	                new ResponseMsg(e.getMessage()),
	                HttpStatus.INTERNAL_SERVER_ERROR
	        );
	    }
	}

	@GetMapping(params = "toAccountId")
	public ResponseEntity<?> byTo(@RequestParam Integer toAccountId) {
	    try {

	        if (toAccountId == null) {
	            return new ResponseEntity<>(
	                    new ResponseMsg("toAccountId is required"),
	                    HttpStatus.BAD_REQUEST
	            );
	        }

	        List<FundTransferDto> result = service.getByToAccountId(toAccountId);

	        return new ResponseEntity<>(result, HttpStatus.OK);

	    } catch (Exception e) {
	        return new ResponseEntity<>(
	                new ResponseMsg(e.getMessage()),
	                HttpStatus.INTERNAL_SERVER_ERROR
	        );
	    }
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.getById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{id}/reverse")
	public ResponseEntity<?> reverse(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(service.reverseTransfer(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/account/{accountId}/statement")
	public ResponseEntity<?> statement(
	        @PathVariable Integer accountId,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

	    try {
	        if (accountId == null) {
	            return new ResponseEntity<>(
	                    new ResponseMsg("AccountId is required"),
	                    HttpStatus.BAD_REQUEST
	            );
	        }

	        if (from == null || to == null) {
	            return new ResponseEntity<>(
	                    new ResponseMsg("Both from and to dates are required"),
	                    HttpStatus.BAD_REQUEST
	            );
	        }

	        if (from.isAfter(to)) {
	            return new ResponseEntity<>(
	                    new ResponseMsg("Invalid date range"),
	                    HttpStatus.BAD_REQUEST
	            );
	        }

	        List<FundTransferDto> result = service.getStatement(accountId, from, to);

	        return new ResponseEntity<>(result, HttpStatus.OK);

	    } catch (Exception e) {

	        return new ResponseEntity<>(
	                new ResponseMsg(e.getMessage()),
	                HttpStatus.INTERNAL_SERVER_ERROR
	        );
	    }
	}
}
