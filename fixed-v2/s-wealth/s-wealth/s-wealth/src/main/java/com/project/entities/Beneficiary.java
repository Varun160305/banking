package com.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.project.enums.BeneficiaryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "beneficiary", uniqueConstraints = @UniqueConstraint(columnNames = { "customerId", "accountNumber" }))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer beneficiaryId;

	private Integer customerId;
	private String accountNumber;
	private String name;
	private String bankName;
	@Enumerated(EnumType.STRING)
	private BeneficiaryStatus status;
}
