package com.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.project.enums.LoanAccountStatus;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loan_account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer loanAccountId;

	@ManyToOne
	@JoinColumn(name = "ApplicationID", unique = true)
	private LoanApplication application;

	private Integer customerId;
	private Double disbursedAmount;
	private Double interestRate;
	private Double emiAmount;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double outstandingBalance;
	@Enumerated(EnumType.STRING)
	private LoanAccountStatus status;
}
