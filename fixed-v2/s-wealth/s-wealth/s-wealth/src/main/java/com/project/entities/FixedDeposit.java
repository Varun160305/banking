package com.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.project.enums.FixedDepositStatus;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fixed_deposit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedDeposit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fdId;

	private Integer customerId;
	private Integer accountId;
	private Double principal;
	private Double interestRate;
	private Integer tenure;
	private LocalDate maturityDate;
	private Double maturityAmount;
	@Enumerated(EnumType.STRING)
	private FixedDepositStatus status;
}
