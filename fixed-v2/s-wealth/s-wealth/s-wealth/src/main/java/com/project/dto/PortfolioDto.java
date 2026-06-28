package com.project.dto;

import java.time.LocalDate;

public class PortfolioDto {

	private Integer portfolioId;
	private Integer customerId;
	private Integer relationshipManagerId;
	private Double totalValue;
	private String assetAllocation;
	private LocalDate createdDate;
	private LocalDate lastReviewedDate;

	public PortfolioDto() {}

	public PortfolioDto(Integer portfolioId, Integer customerId, Integer relationshipManagerId, Double totalValue, String assetAllocation, LocalDate createdDate, LocalDate lastReviewedDate) {
		this.portfolioId = portfolioId;
		this.customerId = customerId;
		this.relationshipManagerId = relationshipManagerId;
		this.totalValue = totalValue;
		this.assetAllocation = assetAllocation;
		this.createdDate = createdDate;
		this.lastReviewedDate = lastReviewedDate;
	}

	public Integer getPortfolioId() { return portfolioId; }
	public void setPortfolioId(Integer portfolioId) { this.portfolioId = portfolioId; }

	public Integer getCustomerId() { return customerId; }
	public void setCustomerId(Integer customerId) { this.customerId = customerId; }

	public Integer getRelationshipManagerId() { return relationshipManagerId; }
	public void setRelationshipManagerId(Integer relationshipManagerId) { this.relationshipManagerId = relationshipManagerId; }

	public Double getTotalValue() { return totalValue; }
	public void setTotalValue(Double totalValue) { this.totalValue = totalValue; }

	public String getAssetAllocation() { return assetAllocation; }
	public void setAssetAllocation(String assetAllocation) { this.assetAllocation = assetAllocation; }

	public LocalDate getCreatedDate() { return createdDate; }
	public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

	public LocalDate getLastReviewedDate() { return lastReviewedDate; }
	public void setLastReviewedDate(LocalDate lastReviewedDate) { this.lastReviewedDate = lastReviewedDate; }
}
