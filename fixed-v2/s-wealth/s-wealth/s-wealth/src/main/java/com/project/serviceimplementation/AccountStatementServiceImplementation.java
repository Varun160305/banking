package com.project.serviceimplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.AccountStatementDto;
import com.project.entities.AccountStatement;
import com.project.entities.BankAccount;
import com.project.repository.AccountStatementRepository;
import com.project.repository.BankAccountRepository;
import com.project.service.AccountStatementService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountStatementServiceImplementation implements AccountStatementService {

	@Autowired
	AccountStatementRepository repo;

	@Autowired
	BankAccountRepository accountRepo;

	@Override
	public AccountStatementDto generateStatement(Integer accountId, LocalDate from, LocalDate to) throws Exception {
		BankAccount account = accountRepo.findById(accountId)
				.orElseThrow(() -> new Exception("BankAccount not found with ID: " + accountId));
		double balance = account.getBalance() == null ? 0.0 : account.getBalance();
		int txnCount = repo.findByBankAccount_AccountId(accountId).size();

		AccountStatement e = AccountStatement.builder()
				.bankAccount(account)
				.periodStart(from)
				.periodEnd(to)
				.openingBalance(balance)
				.closingBalance(balance)
				.transactionCount(txnCount)
				.generatedDate(LocalDate.now())
				.build();
		AccountStatement res = repo.save(e);
		log.info("Statement {} generated for account {} [{} .. {}]", res.getStatementId(), accountId, from, to);
		return toDto(res);
	}

	@Override
	public List<AccountStatementDto> getStatementsByAccount(Integer accountId) {
		List<AccountStatementDto> dtos = new ArrayList<>();
		for (AccountStatement e : repo.findByBankAccount_AccountId(accountId)) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	private AccountStatementDto toDto(AccountStatement e) {
		AccountStatementDto dto = new AccountStatementDto();
		dto.setStatementId(e.getStatementId());
		dto.setAccountId(e.getBankAccount() == null ? null : e.getBankAccount().getAccountId());
		dto.setPeriodStart(e.getPeriodStart());
		dto.setPeriodEnd(e.getPeriodEnd());
		dto.setOpeningBalance(e.getOpeningBalance());
		dto.setClosingBalance(e.getClosingBalance());
		dto.setTransactionCount(e.getTransactionCount());
		dto.setGeneratedDate(e.getGeneratedDate());
		return dto;
	}
}
