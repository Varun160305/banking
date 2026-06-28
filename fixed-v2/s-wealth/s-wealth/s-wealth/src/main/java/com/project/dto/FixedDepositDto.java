package com.project.dto;

import com.project.enums.FixedDepositStatus;

import java.time.LocalDate;

public class FixedDepositDto {

	private Integer fdId;
	private Integer customerId;
	private Integer accountId;
	private Double principal;
	private Double interestRate;
	private Integer tenure;
	private LocalDate maturityDate;
	private Double maturityAmount;
	private FixedDepositStatus status;

	public FixedDepositDto() {}

	public FixedDepositDto(Integer fdId, Integer customerId, Integer accountId, Double principal, Double interestRate, Integer tenure, LocalDate maturityDate, Double maturityAmount, FixedDepositStatus status) {
		this.fdId = fdId;
		this.customerId = customerId;
		this.accountId = accountId;
		this.principal = principal;
		this.interestRate = interestRate;
		this.tenure = tenure;
		this.maturityDate = maturityDate;
		this.maturityAmount = maturityAmount;
		this.status = status;
	}

	public Integer getFdId() { return fdId; }
	public void setFdId(Integer fdId) { this.fdId = fdId; }

	public Integer getCustomerId() { return customerId; }
	public void setCustomerId(Integer customerId) { this.customerId = customerId; }

	public Integer getAccountId() { return accountId; }
	public void setAccountId(Integer accountId) { this.accountId = accountId; }

	public Double getPrincipal() { return principal; }
	public void setPrincipal(Double principal) { this.principal = principal; }

	public Double getInterestRate() { return interestRate; }
	public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }

	public Integer getTenure() { return tenure; }
	public void setTenure(Integer tenure) { this.tenure = tenure; }

	public LocalDate getMaturityDate() { return maturityDate; }
	public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }

	public Double getMaturityAmount() { return maturityAmount; }
	public void setMaturityAmount(Double maturityAmount) { this.maturityAmount = maturityAmount; }

	public FixedDepositStatus getStatus() { return status; }
	public void setStatus(FixedDepositStatus status) { this.status = status; }
}
