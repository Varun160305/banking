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

import java.time.LocalDate;

import com.project.enums.FlagType;
import com.project.enums.Severity;
import com.project.enums.AmlStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aml_flag")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmlFlag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer flagId;

	@ManyToOne
	@JoinColumn(name = "AccountID")
	private BankAccount bankAccount;

	private Integer transactionId;
	@Enumerated(EnumType.STRING)
	private FlagType flagType;
	@Enumerated(EnumType.STRING)
	private Severity severity;
	private LocalDate raisedDate;
	@Enumerated(EnumType.STRING)
	private AmlStatus status;
}
