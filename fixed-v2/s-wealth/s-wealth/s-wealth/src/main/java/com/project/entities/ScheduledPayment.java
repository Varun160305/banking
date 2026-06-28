package com.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.project.enums.Frequency;
import com.project.enums.ScheduledPaymentStatus;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scheduled_payment", uniqueConstraints = @UniqueConstraint(columnNames = { "customerId", "fromAccountId", "beneficiaryId", "frequency" }))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer scheduleId;

	private Integer customerId;
	private Integer fromAccountId;

	@ManyToOne
	@JoinColumn(name = "beneficiaryId")
	private Beneficiary beneficiary;

	private Double amount;
	@Enumerated(EnumType.STRING)
	private Frequency frequency;
	private LocalDate nextRunDate;
	@Enumerated(EnumType.STRING)
	private ScheduledPaymentStatus status;
}
