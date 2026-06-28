package com.project.dto;

import java.time.LocalDate;

import com.project.enums.FlagType;
import com.project.enums.Severity;
import com.project.enums.AmlStatus;

public class AmlFlagDto {

	private Integer flagId;
	private Integer accountId;
	private Integer transactionId;
	private FlagType flagType;
	private Severity severity;
	private LocalDate raisedDate;
	private AmlStatus status;

	public AmlFlagDto() {}

	public AmlFlagDto(Integer flagId, Integer accountId, Integer transactionId, FlagType flagType, Severity severity, LocalDate raisedDate, AmlStatus status) {
		this.flagId = flagId;
		this.accountId = accountId;
		this.transactionId = transactionId;
		this.flagType = flagType;
		this.severity = severity;
		this.raisedDate = raisedDate;
		this.status = status;
	}

	public Integer getFlagId() { return flagId; }
	public void setFlagId(Integer flagId) { this.flagId = flagId; }

	public Integer getAccountId() { return accountId; }
	public void setAccountId(Integer accountId) { this.accountId = accountId; }

	public Integer getTransactionId() { return transactionId; }
	public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }

	public FlagType getFlagType() { return flagType; }
	public void setFlagType(FlagType flagType) { this.flagType = flagType; }

	public Severity getSeverity() { return severity; }
	public void setSeverity(Severity severity) { this.severity = severity; }

	public LocalDate getRaisedDate() { return raisedDate; }
	public void setRaisedDate(LocalDate raisedDate) { this.raisedDate = raisedDate; }

	public AmlStatus getStatus() { return status; }
	public void setStatus(AmlStatus status) { this.status = status; }
}
