package com.project.dto;

import com.project.enums.Frequency;
import com.project.enums.ScheduledPaymentStatus;

import java.time.LocalDate;

public class ScheduledPaymentDto {

	private Integer scheduleId;
	private Integer customerId;
	private Integer fromAccountId;
	private Integer beneficiaryId;
	private Double amount;
	private Frequency frequency;
	private LocalDate nextRunDate;
	private ScheduledPaymentStatus status;

	public ScheduledPaymentDto() {}

	public ScheduledPaymentDto(Integer scheduleId, Integer customerId, Integer fromAccountId, Integer beneficiaryId, Double amount, Frequency frequency, LocalDate nextRunDate, ScheduledPaymentStatus status) {
		this.scheduleId = scheduleId;
		this.customerId = customerId;
		this.fromAccountId = fromAccountId;
		this.beneficiaryId = beneficiaryId;
		this.amount = amount;
		this.frequency = frequency;
		this.nextRunDate = nextRunDate;
		this.status = status;
	}

	public Integer getScheduleId() { return scheduleId; }
	public void setScheduleId(Integer scheduleId) { this.scheduleId = scheduleId; }

	public Integer getCustomerId() { return customerId; }
	public void setCustomerId(Integer customerId) { this.customerId = customerId; }

	public Integer getFromAccountId() { return fromAccountId; }
	public void setFromAccountId(Integer fromAccountId) { this.fromAccountId = fromAccountId; }

	public Integer getBeneficiaryId() { return beneficiaryId; }
	public void setBeneficiaryId(Integer beneficiaryId) { this.beneficiaryId = beneficiaryId; }

	public Double getAmount() { return amount; }
	public void setAmount(Double amount) { this.amount = amount; }

	public Frequency getFrequency() { return frequency; }
	public void setFrequency(Frequency frequency) { this.frequency = frequency; }

	public LocalDate getNextRunDate() { return nextRunDate; }
	public void setNextRunDate(LocalDate nextRunDate) { this.nextRunDate = nextRunDate; }

	public ScheduledPaymentStatus getStatus() { return status; }
	public void setStatus(ScheduledPaymentStatus status) { this.status = status; }
}
