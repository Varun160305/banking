package com.project.dto;

import com.project.enums.AssetType;
import com.project.enums.HoldingStatus;

public class InvestmentHoldingDto {

	private Integer holdingId;
	private Integer portfolioId;
	private AssetType assetType;
	private String assetName;
	private Double units;
	private Double purchaseValue;
	private Double currentValue;
	private HoldingStatus status;

	public InvestmentHoldingDto() {}

	public InvestmentHoldingDto(Integer holdingId, Integer portfolioId, AssetType assetType, String assetName, Double units, Double purchaseValue, Double currentValue, HoldingStatus status) {
		this.holdingId = holdingId;
		this.portfolioId = portfolioId;
		this.assetType = assetType;
		this.assetName = assetName;
		this.units = units;
		this.purchaseValue = purchaseValue;
		this.currentValue = currentValue;
		this.status = status;
	}

	public Integer getHoldingId() { return holdingId; }
	public void setHoldingId(Integer holdingId) { this.holdingId = holdingId; }

	public Integer getPortfolioId() { return portfolioId; }
	public void setPortfolioId(Integer portfolioId) { this.portfolioId = portfolioId; }

	public AssetType getAssetType() { return assetType; }
	public void setAssetType(AssetType assetType) { this.assetType = assetType; }

	public String getAssetName() { return assetName; }
	public void setAssetName(String assetName) { this.assetName = assetName; }

	public Double getUnits() { return units; }
	public void setUnits(Double units) { this.units = units; }

	public Double getPurchaseValue() { return purchaseValue; }
	public void setPurchaseValue(Double purchaseValue) { this.purchaseValue = purchaseValue; }

	public Double getCurrentValue() { return currentValue; }
	public void setCurrentValue(Double currentValue) { this.currentValue = currentValue; }

	public HoldingStatus getStatus() { return status; }
	public void setStatus(HoldingStatus status) { this.status = status; }
}
