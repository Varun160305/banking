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

import com.project.enums.EmiStatus;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "emi_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmiSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer emiId;

	@ManyToOne
	@JoinColumn(name = "LoanAccountID")
	private LoanAccount loanAccount;

	private LocalDate dueDate;
	private Double emiAmount;
	private Double principal;
	private Double interest;
	@Enumerated(EnumType.STRING)
	private EmiStatus status;
}
