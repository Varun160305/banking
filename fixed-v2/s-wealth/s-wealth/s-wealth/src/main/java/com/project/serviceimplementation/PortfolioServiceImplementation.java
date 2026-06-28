package com.project.serviceimplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.InvestmentHoldingDto;
import com.project.dto.PortfolioDto;
import com.project.entities.InvestmentHolding;
import com.project.entities.Portfolio;
import com.project.enums.HoldingStatus;
import com.project.repository.InvestmentHoldingRepository;
import com.project.repository.PortfolioRepository;
import com.project.service.PortfolioService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PortfolioServiceImplementation implements PortfolioService {

	@Autowired
	PortfolioRepository repo;

	@Autowired
	InvestmentHoldingRepository holdingRepo;

	@Override
	public PortfolioDto createPortfolio(PortfolioDto dto) throws Exception {
		Portfolio e = Portfolio.builder()
				.customerId(dto.getCustomerId())
				.relationshipManagerId(dto.getRelationshipManagerId())
				.totalValue(dto.getTotalValue() == null ? 0.0 : dto.getTotalValue())
				.assetAllocation(dto.getAssetAllocation())
				.createdDate(dto.getCreatedDate() == null ? LocalDate.now() : dto.getCreatedDate())
				.lastReviewedDate(dto.getLastReviewedDate())
				.build();
		Portfolio res = repo.save(e);
		log.info("Portfolio {} created for customer {}", res.getPortfolioId(), res.getCustomerId());
		return toDto(res);
	}

	@Override
	public PortfolioDto getById(Integer id) throws Exception {
		Portfolio e = repo.findById(id).orElseThrow(() -> new Exception("Portfolio not found with ID: " + id));
		return toDto(e);
	}

	@Override
	public List<PortfolioDto> getPortfolioByCustomer(Integer customerId) {
		List<PortfolioDto> dtos = new ArrayList<>();
		for (Portfolio e : repo.findByCustomerId(customerId)) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	@Override
	public InvestmentHoldingDto addHolding(Integer portfolioId, InvestmentHoldingDto dto) throws Exception {
		Portfolio portfolio = repo.findById(portfolioId)
				.orElseThrow(() -> new Exception("Portfolio not found with ID: " + portfolioId));
		InvestmentHolding h = InvestmentHolding.builder()
				.portfolio(portfolio)
				.assetType(dto.getAssetType())
				.assetName(dto.getAssetName())
				.units(dto.getUnits())
				.purchaseValue(dto.getPurchaseValue())
				.currentValue(dto.getCurrentValue())
				.status(dto.getStatus() == null ? HoldingStatus.Active : dto.getStatus())
				.build();
		InvestmentHolding res = holdingRepo.save(h);
		recalc(portfolioId);
		log.info("Holding {} added to portfolio {}", res.getHoldingId(), portfolioId);
		return toHoldingDto(res);
	}

	@Override
	public List<InvestmentHoldingDto> getHoldings(Integer portfolioId) {
		List<InvestmentHoldingDto> dtos = new ArrayList<>();
		for (InvestmentHolding h : holdingRepo.findByPortfolio_PortfolioId(portfolioId)) {
			dtos.add(toHoldingDto(h));
		}
		return dtos;
	}

	@Override
	public InvestmentHoldingDto redeemHolding(Integer holdingId) throws Exception {
		InvestmentHolding h = holdingRepo.findById(holdingId)
				.orElseThrow(() -> new Exception("InvestmentHolding not found with ID: " + holdingId));
		h.setStatus(HoldingStatus.Redeemed);
		InvestmentHolding res = holdingRepo.save(h);
		if (res.getPortfolio() != null) {
			recalc(res.getPortfolio().getPortfolioId());
		}
		log.info("Holding {} redeemed", holdingId);
		return toHoldingDto(res);
	}

	@Override
	public PortfolioDto updatePortfolioValue(Integer portfolioId) throws Exception {
		Portfolio portfolio = repo.findById(portfolioId)
				.orElseThrow(() -> new Exception("Portfolio not found with ID: " + portfolioId));
		double total = 0.0;
		for (InvestmentHolding h : holdingRepo.findByPortfolio_PortfolioId(portfolioId)) {
			if (h.getStatus() == HoldingStatus.Active && h.getCurrentValue() != null) {
				total += h.getCurrentValue();
			}
		}
		portfolio.setTotalValue(total);
		Portfolio res = repo.save(portfolio);
		return toDto(res);
	}

	/** Recalculate and persist a portfolio's total value from its active holdings. */
	private void recalc(Integer portfolioId) {
		repo.findById(portfolioId).ifPresent(portfolio -> {
			double total = 0.0;
			for (InvestmentHolding h : holdingRepo.findByPortfolio_PortfolioId(portfolioId)) {
				if (h.getStatus() == HoldingStatus.Active && h.getCurrentValue() != null) {
					total += h.getCurrentValue();
				}
			}
			portfolio.setTotalValue(total);
			repo.save(portfolio);
		});
	}

	private PortfolioDto toDto(Portfolio e) {
		PortfolioDto dto = new PortfolioDto();
		dto.setPortfolioId(e.getPortfolioId());
		dto.setCustomerId(e.getCustomerId());
		dto.setRelationshipManagerId(e.getRelationshipManagerId());
		dto.setTotalValue(e.getTotalValue());
		dto.setAssetAllocation(e.getAssetAllocation());
		dto.setCreatedDate(e.getCreatedDate());
		dto.setLastReviewedDate(e.getLastReviewedDate());
		return dto;
	}

	private InvestmentHoldingDto toHoldingDto(InvestmentHolding e) {
		InvestmentHoldingDto dto = new InvestmentHoldingDto();
		dto.setHoldingId(e.getHoldingId());
		dto.setPortfolioId(e.getPortfolio() == null ? null : e.getPortfolio().getPortfolioId());
		dto.setAssetType(e.getAssetType());
		dto.setAssetName(e.getAssetName());
		dto.setUnits(e.getUnits());
		dto.setPurchaseValue(e.getPurchaseValue());
		dto.setCurrentValue(e.getCurrentValue());
		dto.setStatus(e.getStatus());
		return dto;
	}
}
