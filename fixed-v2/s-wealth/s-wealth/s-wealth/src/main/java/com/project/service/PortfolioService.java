package com.project.service;

import java.util.List;

import com.project.dto.InvestmentHoldingDto;
import com.project.dto.PortfolioDto;

public interface PortfolioService {

	PortfolioDto createPortfolio(PortfolioDto dto) throws Exception;

	PortfolioDto getById(Integer id) throws Exception;

	List<PortfolioDto> getPortfolioByCustomer(Integer customerId);

	InvestmentHoldingDto addHolding(Integer portfolioId, InvestmentHoldingDto holding) throws Exception;

	List<InvestmentHoldingDto> getHoldings(Integer portfolioId);

	InvestmentHoldingDto redeemHolding(Integer holdingId) throws Exception;

	PortfolioDto updatePortfolioValue(Integer portfolioId) throws Exception;
}
