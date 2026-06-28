package com.project.dto;

import com.project.enums.LoanAccountStatus;

import java.time.LocalDate;

public class LoanAccountDto {

	private Integer loanAccountId;
	private Integer applicationId;
	private Integer customerId;
	private Double disbursedAmount;
	private Double interestRate;
	private Double emiAmount;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double outstandingBalance;
	private LoanAccountStatus status;

	public LoanAccountDto() {}

	public LoanAccountDto(Integer loanAccountId, Integer applicationId, Integer customerId, Double disbursedAmount, Double interestRate, Double emiAmount, LocalDate startDate, LocalDate endDate, Double outstandingBalance, LoanAccountStatus status) {
		this.loanAccountId = loanAccountId;
		this.applicationId = applicationId;
		this.customerId = customerId;
		this.disbursedAmount = disbursedAmount;
		this.interestRate = interestRate;
		this.emiAmount = emiAmount;
		this.startDate = startDate;
		this.endDate = endDate;
		this.outstandingBalance = outstandingBalance;
		this.status = status;
	}

	public Integer getLoanAccountId() { return loanAccountId; }
	public void setLoanAccountId(Integer loanAccountId) { this.loanAccountId = loanAccountId; }

	public Integer getApplicationId() { return applicationId; }
	public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }

	public Integer getCustomerId() { return customerId; }
	public void setCustomerId(Integer customerId) { this.customerId = customerId; }

	public Double getDisbursedAmount() { return disbursedAmount; }
	public void setDisbursedAmount(Double disbursedAmount) { this.disbursedAmount = disbursedAmount; }

	public Double getInterestRate() { return interestRate; }
	public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }

	public Double getEmiAmount() { return emiAmount; }
	public void setEmiAmount(Double emiAmount) { this.emiAmount = emiAmount; }

	public LocalDate getStartDate() { return startDate; }
	public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

	public LocalDate getEndDate() { return endDate; }
	public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

	public Double getOutstandingBalance() { return outstandingBalance; }
	public void setOutstandingBalance(Double outstandingBalance) { this.outstandingBalance = outstandingBalance; }

	public LoanAccountStatus getStatus() { return status; }
	public void setStatus(LoanAccountStatus status) { this.status = status; }
}
