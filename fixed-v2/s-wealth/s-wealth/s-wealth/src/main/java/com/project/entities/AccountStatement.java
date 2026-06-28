package com.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_statement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer statementId;

	@ManyToOne
	@JoinColumn(name = "AccountID")
	private BankAccount bankAccount;

	private LocalDate periodStart;
	private LocalDate periodEnd;
	private Double openingBalance;
	private Double closingBalance;
	private Integer transactionCount;
	private LocalDate generatedDate;
}
