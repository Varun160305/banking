package com.project.serviceimplementation;

import java.time.LocalDate;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.dto.BankAccountDto;
import com.project.entities.BankAccount;
import com.project.entities.User;
import com.project.enums.AccountStatus;
import com.project.enums.Role;
import com.project.repository.BankAccountRepository;
import com.project.repository.UserRepository;
import com.project.service.BankAccountService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankAccountServiceImplementation implements BankAccountService {

	@Autowired
	BankAccountRepository repo;
	
	@Autowired
	UserRepository userRepo;

	@Override
	public BankAccountDto openAccount(BankAccountDto dto) throws Exception {
		if (repo.existsByAccountNumber(dto.getAccountNumber())) {
			throw new Exception("A bank account already exists with account number: " + dto.getAccountNumber());
		}
		BankAccount e = BankAccount.builder()
				.customerId(dto.getCustomerId())
				.accountType(dto.getAccountType())
				.accountNumber(dto.getAccountNumber())
				.balance(dto.getBalance() == null ? 0.0 : dto.getBalance())
				.interestRate(dto.getInterestRate())
				.openDate(dto.getOpenDate() == null ? LocalDate.now() : dto.getOpenDate())
				.status(dto.getStatus() == null ? AccountStatus.Active : dto.getStatus())
				.build();
		BankAccount res = repo.save(e);
		log.info("Bank account {} opened ({}) for customer {}", res.getAccountId(), res.getAccountNumber(), res.getCustomerId());
		return toDto(res);
	}

	@Override
	public BankAccountDto getById(Integer id) throws Exception {
		BankAccount e = repo.findById(id).orElseThrow(() -> new Exception("BankAccount not found with ID: " + id));
		return toDto(e);
	}

	@Override
	public BankAccountDto getAccountByNumber(String accountNumber) throws Exception {
		BankAccount e = repo.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new Exception("BankAccount not found with number: " + accountNumber));
		return toDto(e);
	}

	@Override
	public List<BankAccountDto> getAccountsByCustomer(Integer customerId) {
		List<BankAccountDto> dtos = new ArrayList<>();
		for (BankAccount e : repo.findByCustomerId(customerId)) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	@Override
	public BankAccountDto updateBalance(Integer accountId, double amount, boolean credit) throws Exception {
		BankAccount e = repo.findById(accountId).orElseThrow(() -> new Exception("BankAccount not found with ID: " + accountId));
		double balance = e.getBalance() == null ? 0.0 : e.getBalance();
		if (credit) {
			balance += amount;
		} else {
			if (balance < amount) {
				throw new Exception("Insufficient balance in account " + accountId + ".");
			}
			balance -= amount;
		}
		e.setBalance(balance);
		BankAccount res = repo.save(e);
		log.info("Account {} {} {} -> balance {}", accountId, credit ? "credited" : "debited", amount, balance);
		return toDto(res);
	}

	@Override
	public BankAccountDto closeAccount(Integer accountId) throws Exception {
		BankAccount e = repo.findById(accountId).orElseThrow(() -> new Exception("BankAccount not found with ID: " + accountId));
		e.setStatus(AccountStatus.Closed);
		BankAccount res = repo.save(e);
		log.info("Account {} closed", accountId);
		return toDto(res);
	}

	private BankAccountDto toDto(BankAccount e) {
		BankAccountDto dto = new BankAccountDto();
		dto.setAccountId(e.getAccountId());
		dto.setCustomerId(e.getCustomerId());
		dto.setAccountType(e.getAccountType());
		dto.setAccountNumber(e.getAccountNumber());
		dto.setBalance(e.getBalance());
		dto.setInterestRate(e.getInterestRate());
		dto.setOpenDate(e.getOpenDate());
		dto.setStatus(e.getStatus());
		return dto;
	}



	@Override
	public BankAccountDto deposit(Integer accountId, double amount) throws Exception {

	    if (amount <= 0) {
	        throw new Exception("Deposit amount must be greater than zero");
	    }

	    String email = SecurityContextHolder.getContext()
	            .getAuthentication().getName();

	    User user = userRepo.findByEmail(email)
	            .orElseThrow(() -> new Exception("User not found"));

	    BankAccount acc = repo.findById(accountId)
	            .orElseThrow(() -> new Exception("Account not found"));


if (!acc.getCustomerId().equals(user.getUserId())
        && user.getRole() != Role.ADMIN) {

    throw new Exception("Unauthorized");
}


	    return updateBalance(accountId, amount, true);
	}



}
