package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.InvestmentHolding;
import com.project.enums.AssetType;
import com.project.enums.HoldingStatus;

@Repository
public interface InvestmentHoldingRepository extends JpaRepository<InvestmentHolding, Integer> {

	List<InvestmentHolding> findByPortfolio_PortfolioId(Integer portfolioId);

	List<InvestmentHolding> findByAssetType(AssetType assetType);

	List<InvestmentHolding> findByStatus(HoldingStatus status);
}
