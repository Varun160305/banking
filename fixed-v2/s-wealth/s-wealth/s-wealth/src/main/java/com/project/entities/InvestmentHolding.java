package com.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.project.enums.AssetType;
import com.project.enums.HoldingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "investment_holding")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentHolding {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer holdingId;

	@ManyToOne
	@JoinColumn(name = "PortfolioID")
	private Portfolio portfolio;

	@Enumerated(EnumType.STRING)
	private AssetType assetType;
	@Column(length = 200)
	private String assetName;
	private Double units;
	private Double purchaseValue;
	private Double currentValue;
	@Enumerated(EnumType.STRING)
	private HoldingStatus status;
}
