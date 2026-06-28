package com.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

import com.project.enums.AccountType;
import com.project.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accountId;

	private Integer customerId;
	@Enumerated(EnumType.STRING)
	private AccountType accountType;

	@Column(unique = true, nullable = false, length = 20)
	private String accountNumber;
	private Double balance;
	private Double interestRate;
	private LocalDate openDate;
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
}
