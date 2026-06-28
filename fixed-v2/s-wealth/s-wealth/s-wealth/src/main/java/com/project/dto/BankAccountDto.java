package com.project.dto;

import java.time.LocalDate;

import com.project.enums.AccountType;
import com.project.enums.AccountStatus;

public class BankAccountDto {

	private Integer accountId;
	private Integer customerId;
	private AccountType accountType;
	private String accountNumber;
	private Double balance;
	private Double interestRate;
	private LocalDate openDate;
	private AccountStatus status;

	public BankAccountDto() {}

	public BankAccountDto(Integer accountId, Integer customerId, AccountType accountType, String accountNumber, Double balance, Double interestRate, LocalDate openDate, AccountStatus status) {
		this.accountId = accountId;
		this.customerId = customerId;
		this.accountType = accountType;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.interestRate = interestRate;
		this.openDate = openDate;
		this.status = status;
	}

	public Integer getAccountId() { return accountId; }
	public void setAccountId(Integer accountId) { this.accountId = accountId; }

	public Integer getCustomerId() { return customerId; }
	public void setCustomerId(Integer customerId) { this.customerId = customerId; }

	public AccountType getAccountType() { return accountType; }
	public void setAccountType(AccountType accountType) { this.accountType = accountType; }

	public String getAccountNumber() { return accountNumber; }
	public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

	public Double getBalance() { return balance; }
	public void setBalance(Double balance) { this.balance = balance; }

	public Double getInterestRate() { return interestRate; }
	public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }

	public LocalDate getOpenDate() { return openDate; }
	public void setOpenDate(LocalDate openDate) { this.openDate = openDate; }

	public AccountStatus getStatus() { return status; }
	public void setStatus(AccountStatus status) { this.status = status; }
}
