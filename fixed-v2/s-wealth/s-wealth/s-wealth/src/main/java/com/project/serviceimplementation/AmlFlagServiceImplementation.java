package com.project.serviceimplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.AmlFlagDto;
import com.project.entities.AmlFlag;
import com.project.entities.BankAccount;
import com.project.enums.AmlStatus;
import com.project.repository.AmlFlagRepository;
import com.project.repository.BankAccountRepository;
import com.project.service.AmlFlagService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AmlFlagServiceImplementation implements AmlFlagService {

	@Autowired
	AmlFlagRepository repo;

	@Autowired
	BankAccountRepository accountRepo;

	@Override
	public AmlFlagDto raiseFlag(AmlFlagDto dto) throws Exception {
		BankAccount account = accountRepo.findById(dto.getAccountId())
				.orElseThrow(() -> new Exception("BankAccount not found with ID: " + dto.getAccountId()));
		AmlFlag e = AmlFlag.builder()
				.bankAccount(account)
				.transactionId(dto.getTransactionId())
				.flagType(dto.getFlagType())
				.severity(dto.getSeverity())
				.raisedDate(dto.getRaisedDate() == null ? LocalDate.now() : dto.getRaisedDate())
				.status(AmlStatus.Open)
				.build();
		AmlFlag res = repo.save(e);
		log.info("AML flag {} raised on account {} ({})", res.getFlagId(), account.getAccountId(), res.getFlagType());
		return toDto(res);
	}

	@Override
	public AmlFlagDto investigateFlag(Integer id) throws Exception {
		AmlFlag e = repo.findById(id).orElseThrow(() -> new Exception("AmlFlag not found with ID: " + id));
		e.setStatus(AmlStatus.Investigated);
		AmlFlag res = repo.save(e);
		log.info("AML flag {} -> INVESTIGATED", id);
		return toDto(res);
	}

	@Override
	public AmlFlagDto clearFlag(Integer id) throws Exception {
		AmlFlag e = repo.findById(id).orElseThrow(() -> new Exception("AmlFlag not found with ID: " + id));
		e.setStatus(AmlStatus.Cleared);
		AmlFlag res = repo.save(e);
		log.info("AML flag {} -> CLEARED", id);
		return toDto(res);
	}

	@Override
	public List<AmlFlagDto> getOpenFlags() {
		return toDtoList(repo.findByStatus(AmlStatus.Open));
	}

	@Override
	public List<AmlFlagDto> getByStatus(AmlStatus status) {
		return toDtoList(repo.findByStatus(status));
	}

	@Override
	public List<AmlFlagDto> getFlagsByAccount(Integer accountId) {
		return toDtoList(repo.findByBankAccount_AccountId(accountId));
	}

	private List<AmlFlagDto> toDtoList(List<AmlFlag> list) {
		List<AmlFlagDto> dtos = new ArrayList<>();
		for (AmlFlag e : list) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	private AmlFlagDto toDto(AmlFlag e) {
		AmlFlagDto dto = new AmlFlagDto();
		dto.setFlagId(e.getFlagId());
		dto.setAccountId(e.getBankAccount() == null ? null : e.getBankAccount().getAccountId());
		dto.setTransactionId(e.getTransactionId());
		dto.setFlagType(e.getFlagType());
		dto.setSeverity(e.getSeverity());
		dto.setRaisedDate(e.getRaisedDate());
		dto.setStatus(e.getStatus());
		return dto;
	}
}
