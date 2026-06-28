package com.project.dto;

import java.time.LocalDate;

public class AccountStatementDto {

	private Integer statementId;
	private Integer accountId;
	private LocalDate periodStart;
	private LocalDate periodEnd;
	private Double openingBalance;
	private Double closingBalance;
	private Integer transactionCount;
	private LocalDate generatedDate;

	public AccountStatementDto() {}

	public AccountStatementDto(Integer statementId, Integer accountId, LocalDate periodStart, LocalDate periodEnd, Double openingBalance, Double closingBalance, Integer transactionCount, LocalDate generatedDate) {
		this.statementId = statementId;
		this.accountId = accountId;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
		this.openingBalance = openingBalance;
		this.closingBalance = closingBalance;
		this.transactionCount = transactionCount;
		this.generatedDate = generatedDate;
	}

	public Integer getStatementId() { return statementId; }
	public void setStatementId(Integer statementId) { this.statementId = statementId; }

	public Integer getAccountId() { return accountId; }
	public void setAccountId(Integer accountId) { this.accountId = accountId; }

	public LocalDate getPeriodStart() { return periodStart; }
	public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }

	public LocalDate getPeriodEnd() { return periodEnd; }
	public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }

	public Double getOpeningBalance() { return openingBalance; }
	public void setOpeningBalance(Double openingBalance) { this.openingBalance = openingBalance; }

	public Double getClosingBalance() { return closingBalance; }
	public void setClosingBalance(Double closingBalance) { this.closingBalance = closingBalance; }

	public Integer getTransactionCount() { return transactionCount; }
	public void setTransactionCount(Integer transactionCount) { this.transactionCount = transactionCount; }

	public LocalDate getGeneratedDate() { return generatedDate; }
	public void setGeneratedDate(LocalDate generatedDate) { this.generatedDate = generatedDate; }
}
