package com.project.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entities.AccountStatement;
import com.project.entities.BankAccount;
import com.project.enums.AccountStatus;
import com.project.repository.AccountStatementRepository;
import com.project.repository.BankAccountRepository;

/**
 * Central ledger. All money movement goes through here so that balances and the
 * per-account daily statement stay consistent no matter which module triggers
 * the transaction (transfers, fixed deposits, loan disbursement, scheduled
 * payments, ...).
 */
@Service
public class LedgerService {

	@Autowired
	private BankAccountRepository accountRepo;

	@Autowired
	private AccountStatementRepository statementRepo;

	/** Debit an account (must exist, be active, and have sufficient funds) and post to its statement. */
	public BankAccount debit(Integer accountId, double amount) throws Exception {
		BankAccount acc = accountRepo.findById(accountId)
				.orElseThrow(() -> new Exception("Account not found with ID: " + accountId));
		if (acc.getStatus() != AccountStatus.Active) {
			throw new Exception("Account " + accountId + " is not active; debit rejected.");
		}
		double opening = acc.getBalance() == null ? 0.0 : acc.getBalance();
		if (opening < amount) {
			throw new Exception("Insufficient balance in account " + accountId + ".");
		}
		acc.setBalance(opening - amount);
		accountRepo.save(acc);
		postToStatement(acc, opening, acc.getBalance());
		return acc;
	}

	/** Credit an account (must exist and be active) and post to its statement. */
	public BankAccount credit(Integer accountId, double amount) throws Exception {
		BankAccount acc = accountRepo.findById(accountId)
				.orElseThrow(() -> new Exception("Account not found with ID: " + accountId));
		if (acc.getStatus() != AccountStatus.Active) {
			throw new Exception("Account " + accountId + " is not active; credit rejected.");
		}
		double opening = acc.getBalance() == null ? 0.0 : acc.getBalance();
		acc.setBalance(opening + amount);
		accountRepo.save(acc);
		postToStatement(acc, opening, acc.getBalance());
		return acc;
	}

	/**
	 * Upsert today's running statement for an account: created on the first
	 * transaction of the day, otherwise the closing balance and transaction count
	 * are updated. Keyed on (accountId, today, today) to respect the unique
	 * constraint on AccountStatement.
	 */
	private void postToStatement(BankAccount account, double opening, double closing) {
		LocalDate today = LocalDate.now();
		AccountStatement stmt = statementRepo
				.findByBankAccount_AccountIdAndPeriodStartAndPeriodEnd(account.getAccountId(), today, today)
				.orElse(null);
		if (stmt == null) {
			stmt = AccountStatement.builder()
					.bankAccount(account)
					.periodStart(today)
					.periodEnd(today)
					.openingBalance(opening)
					.closingBalance(closing)
					.transactionCount(1)
					.generatedDate(today)
					.build();
		} else {
			stmt.setClosingBalance(closing);
			stmt.setTransactionCount((stmt.getTransactionCount() == null ? 0 : stmt.getTransactionCount()) + 1);
			stmt.setGeneratedDate(today);
		}
		statementRepo.save(stmt);
	}
}
