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

import com.project.enums.DocumentType;
import com.project.enums.KycStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kyc_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer kycId;

	private Integer customerId;
	@Enumerated(EnumType.STRING)
	private DocumentType documentType;

	@Column(length = 100)
	private String documentNumber;
	private Integer verifiedBy;
	private LocalDate verificationDate;
	@Enumerated(EnumType.STRING)
	private KycStatus status;
}
