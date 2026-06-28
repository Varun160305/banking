package com.project.serviceimplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.FixedDepositDto;
import com.project.entities.BankAccount;
import com.project.entities.FixedDeposit;
import com.project.enums.AuditAction;
import com.project.enums.AuditModule;
import com.project.enums.FixedDepositStatus;
import com.project.enums.NotificationCategory;
import com.project.repository.FixedDepositRepository;
import com.project.service.AuditTrailService;
import com.project.service.FixedDepositService;
import com.project.service.LedgerService;
import com.project.service.NotifierService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FixedDepositServiceImplementation implements FixedDepositService {

	@Autowired
	FixedDepositRepository repo;

	@Autowired
	LedgerService ledger;

	@Autowired
	NotifierService notifier;

	@Autowired
	AuditTrailService auditTrail;

	@Override
	@Transactional
	public FixedDepositDto createFD(FixedDepositDto dto) throws Exception {
		if (dto.getPrincipal() == null || dto.getPrincipal() <= 0) {
			throw new Exception("Principal must be greater than zero.");
		}
		// cross-module: debit the principal from the linked bank account (updates balance + statement)
		BankAccount acc = ledger.debit(dto.getAccountId(), dto.getPrincipal());

		FixedDeposit e = FixedDeposit.builder()
				.customerId(dto.getCustomerId())
				.accountId(dto.getAccountId())
				.principal(dto.getPrincipal())
				.interestRate(dto.getInterestRate())
				.tenure(dto.getTenure())
				.maturityDate(dto.getMaturityDate())
				.maturityAmount(dto.getMaturityAmount())
				.status(dto.getStatus() == null ? FixedDepositStatus.Active : dto.getStatus())
				.build();
		FixedDeposit res = repo.save(e);

		notifier.notify(acc.getCustomerId(),
				"Fixed deposit of " + dto.getPrincipal() + " opened; debited from account " + dto.getAccountId() + ".",
				NotificationCategory.Investment);
		auditTrail.record(AuditAction.CREATE, AuditModule.INVESTMENT_MODULE);
		log.info("Fixed deposit {} created (principal {} debited from account {})", res.getFdId(), dto.getPrincipal(), dto.getAccountId());
		return toDto(res);
	}

	@Override
	public FixedDepositDto getById(Integer id) throws Exception {
		FixedDeposit e = repo.findById(id).orElseThrow(() -> new Exception("FixedDeposit not found with ID: " + id));
		return toDto(e);
	}

	@Override
	public List<FixedDepositDto> getByCustomer(Integer customerId) {
		return toDtoList(repo.findByCustomerId(customerId));
	}

	@Override
	@Transactional
	public FixedDepositDto matureFD(Integer id) throws Exception {
		FixedDeposit e = repo.findById(id).orElseThrow(() -> new Exception("FixedDeposit not found with ID: " + id));
		if (e.getStatus() == FixedDepositStatus.Matured) {
			throw new Exception("Fixed deposit " + id + " is already MATURED.");
		}
		e.setStatus(FixedDepositStatus.Matured);
		FixedDeposit res = repo.save(e);

		// cross-module: credit the maturity amount back to the linked account
		if (e.getMaturityAmount() != null) {
			BankAccount acc = ledger.credit(e.getAccountId(), e.getMaturityAmount());
			notifier.notify(acc.getCustomerId(),
					"Fixed deposit #" + id + " matured; " + e.getMaturityAmount() + " credited to account " + e.getAccountId() + ".",
					NotificationCategory.Investment);
		}
		auditTrail.record(AuditAction.UPDATE, AuditModule.INVESTMENT_MODULE);
		log.info("Fixed deposit {} matured", id);
		return toDto(res);
	}

	@Override
	public FixedDepositDto closeFD(Integer id) throws Exception {
		FixedDeposit e = repo.findById(id).orElseThrow(() -> new Exception("FixedDeposit not found with ID: " + id));
		e.setStatus(FixedDepositStatus.PrematurelyClosed);
		FixedDeposit res = repo.save(e);
		auditTrail.record(AuditAction.UPDATE, AuditModule.INVESTMENT_MODULE);
		log.info("Fixed deposit {} prematurely closed", id);
		return toDto(res);
	}

	@Override
	public List<FixedDepositDto> getMaturedFDs() {
		return toDtoList(repo.findByStatus(FixedDepositStatus.Matured));
	}

	@Override
	public List<FixedDepositDto> getMaturing(LocalDate before) {
		return toDtoList(repo.findByMaturityDateBefore(before));
	}

	private List<FixedDepositDto> toDtoList(List<FixedDeposit> list) {
		List<FixedDepositDto> dtos = new ArrayList<>();
		for (FixedDeposit e : list) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	private FixedDepositDto toDto(FixedDeposit e) {
		FixedDepositDto dto = new FixedDepositDto();
		dto.setFdId(e.getFdId());
		dto.setCustomerId(e.getCustomerId());
		dto.setAccountId(e.getAccountId());
		dto.setPrincipal(e.getPrincipal());
		dto.setInterestRate(e.getInterestRate());
		dto.setTenure(e.getTenure());
		dto.setMaturityDate(e.getMaturityDate());
		dto.setMaturityAmount(e.getMaturityAmount());
		dto.setStatus(e.getStatus());
		return dto;
	}
}
