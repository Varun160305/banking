package com.project.dto;

import com.project.enums.LoanType;
import com.project.enums.LoanApplicationStatus;

import java.time.LocalDate;

public class LoanApplicationDto {

	private Integer applicationId;
	private Integer customerId;
	private LoanType loanType;
	private Double requestedAmount;
	private Integer tenure;
	private String purpose;
	private LocalDate applicationDate;
	private LoanApplicationStatus status;

	public LoanApplicationDto() {}

	public LoanApplicationDto(Integer applicationId, Integer customerId, LoanType loanType, Double requestedAmount, Integer tenure, String purpose, LocalDate applicationDate, LoanApplicationStatus status) {
		this.applicationId = applicationId;
		this.customerId = customerId;
		this.loanType = loanType;
		this.requestedAmount = requestedAmount;
		this.tenure = tenure;
		this.purpose = purpose;
		this.applicationDate = applicationDate;
		this.status = status;
	}

	public Integer getApplicationId() { return applicationId; }
	public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }

	public Integer getCustomerId() { return customerId; }
	public void setCustomerId(Integer customerId) { this.customerId = customerId; }

	public LoanType getLoanType() { return loanType; }
	public void setLoanType(LoanType loanType) { this.loanType = loanType; }

	public Double getRequestedAmount() { return requestedAmount; }
	public void setRequestedAmount(Double requestedAmount) { this.requestedAmount = requestedAmount; }

	public Integer getTenure() { return tenure; }
	public void setTenure(Integer tenure) { this.tenure = tenure; }

	public String getPurpose() { return purpose; }
	public void setPurpose(String purpose) { this.purpose = purpose; }

	public LocalDate getApplicationDate() { return applicationDate; }
	public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }

	public LoanApplicationStatus getStatus() { return status; }
	public void setStatus(LoanApplicationStatus status) { this.status = status; }
}
