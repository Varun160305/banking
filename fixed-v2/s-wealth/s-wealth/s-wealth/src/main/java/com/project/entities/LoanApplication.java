package com.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.project.enums.LoanType;
import com.project.enums.LoanApplicationStatus;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loan_application")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer applicationId;

	private Integer customerId;
	@Enumerated(EnumType.STRING)
	private LoanType loanType;
	private Double requestedAmount;
	private Integer tenure;
	@Column(columnDefinition = "TEXT")
	private String purpose;
	private LocalDate applicationDate;
	@Enumerated(EnumType.STRING)
	private LoanApplicationStatus status;
}
