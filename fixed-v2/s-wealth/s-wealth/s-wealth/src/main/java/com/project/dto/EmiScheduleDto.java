package com.project.dto;

import com.project.enums.EmiStatus;

import java.time.LocalDate;

public class EmiScheduleDto {

	private Integer emiId;
	private Integer loanAccountId;
	private LocalDate dueDate;
	private Double emiAmount;
	private Double principal;
	private Double interest;
	private EmiStatus status;

	public EmiScheduleDto() {}

	public EmiScheduleDto(Integer emiId, Integer loanAccountId, LocalDate dueDate, Double emiAmount, Double principal, Double interest, EmiStatus status) {
		this.emiId = emiId;
		this.loanAccountId = loanAccountId;
		this.dueDate = dueDate;
		this.emiAmount = emiAmount;
		this.principal = principal;
		this.interest = interest;
		this.status = status;
	}

	public Integer getEmiId() { return emiId; }
	public void setEmiId(Integer emiId) { this.emiId = emiId; }

	public Integer getLoanAccountId() { return loanAccountId; }
	public void setLoanAccountId(Integer loanAccountId) { this.loanAccountId = loanAccountId; }

	public LocalDate getDueDate() { return dueDate; }
	public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

	public Double getEmiAmount() { return emiAmount; }
	public void setEmiAmount(Double emiAmount) { this.emiAmount = emiAmount; }

	public Double getPrincipal() { return principal; }
	public void setPrincipal(Double principal) { this.principal = principal; }

	public Double getInterest() { return interest; }
	public void setInterest(Double interest) { this.interest = interest; }

	public EmiStatus getStatus() { return status; }
	public void setStatus(EmiStatus status) { this.status = status; }
}
