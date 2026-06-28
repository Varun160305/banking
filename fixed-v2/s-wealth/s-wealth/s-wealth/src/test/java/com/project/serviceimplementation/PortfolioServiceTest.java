package com.project.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.dto.InvestmentHoldingDto;
import com.project.dto.PortfolioDto;
import com.project.entities.InvestmentHolding;
import com.project.entities.Portfolio;
import com.project.enums.AssetType;
import com.project.enums.HoldingStatus;
import com.project.repository.InvestmentHoldingRepository;
import com.project.repository.PortfolioRepository;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

	@Mock
	PortfolioRepository repo;

	@Mock
	InvestmentHoldingRepository holdingRepo;

	@InjectMocks
	PortfolioServiceImplementation service;

	@Test
	void createPortfolio_valid() throws Exception {
		when(repo.save(any(Portfolio.class))).thenAnswer(inv -> {
			Portfolio p = inv.getArgument(0);
			if (p.getPortfolioId() == null) {
				p.setPortfolioId(1);
			}
			return p;
		});

		PortfolioDto dto = new PortfolioDto();
		dto.setCustomerId(101);
		dto.setRelationshipManagerId(3);

		PortfolioDto result = service.createPortfolio(dto);

		assertEquals(1, result.getPortfolioId());
		assertEquals(0.0, result.getTotalValue());
	}

	@Test
	void addHolding_updatesTotalValue() throws Exception {
		Portfolio portfolio = Portfolio.builder().portfolioId(1).customerId(101).totalValue(0.0).build();
		when(repo.findById(1)).thenReturn(Optional.of(portfolio));
		when(holdingRepo.save(any(InvestmentHolding.class))).thenAnswer(inv -> inv.getArgument(0));
		// recalc reads the portfolio's active holdings
		InvestmentHolding active = InvestmentHolding.builder().holdingId(1).currentValue(235000.0)
				.status(HoldingStatus.Active).build();
		when(holdingRepo.findByPortfolio_PortfolioId(1)).thenReturn(List.of(active));
		when(repo.save(any(Portfolio.class))).thenAnswer(inv -> inv.getArgument(0));

		InvestmentHoldingDto dto = new InvestmentHoldingDto();
		dto.setAssetType(AssetType.MutualFund);
		dto.setAssetName("BlueChip");
		dto.setCurrentValue(235000.0);

		service.addHolding(1, dto);

		assertEquals(235000.0, portfolio.getTotalValue());
	}

	@Test
	void redeemHolding_setsRedeemedStatus() throws Exception {
		Portfolio portfolio = Portfolio.builder().portfolioId(1).totalValue(100.0).build();
		InvestmentHolding holding = InvestmentHolding.builder().holdingId(9).portfolio(portfolio)
				.currentValue(5000.0).status(HoldingStatus.Active).build();
		when(holdingRepo.findById(9)).thenReturn(Optional.of(holding));
		when(holdingRepo.save(any(InvestmentHolding.class))).thenAnswer(inv -> inv.getArgument(0));
		when(repo.findById(1)).thenReturn(Optional.of(portfolio));
		when(holdingRepo.findByPortfolio_PortfolioId(eq(1))).thenReturn(List.of());
		when(repo.save(any(Portfolio.class))).thenAnswer(inv -> inv.getArgument(0));

		InvestmentHoldingDto result = service.redeemHolding(9);

		assertEquals(HoldingStatus.Redeemed, result.getStatus());
	}
}
