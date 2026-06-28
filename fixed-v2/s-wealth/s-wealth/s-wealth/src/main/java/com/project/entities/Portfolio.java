package com.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "portfolio")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer portfolioId;

	private Integer customerId;
	private Integer relationshipManagerId;
	private Double totalValue;
	@Column(columnDefinition = "TEXT")
	private String assetAllocation;
	private LocalDate createdDate;
	private LocalDate lastReviewedDate;
}
