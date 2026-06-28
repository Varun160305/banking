package com.project.serviceimplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.LoanApplicationDto;
import com.project.entities.LoanApplication;
import com.project.enums.LoanApplicationStatus;
import com.project.repository.LoanApplicationRepository;
import com.project.service.LoanApplicationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoanApplicationServiceImplementation implements LoanApplicationService {

	@Autowired
	LoanApplicationRepository repo;

	@Override
	public LoanApplicationDto submitApplication(LoanApplicationDto dto) throws Exception {
		if (dto.getCustomerId() == null) {
			throw new Exception("customerId is required.");
		}
		if (dto.getRequestedAmount() == null || dto.getRequestedAmount() <= 0) {
			throw new Exception("requestedAmount must be greater than zero.");
		}
		LoanApplication e = LoanApplication.builder()
				.customerId(dto.getCustomerId())
				.loanType(dto.getLoanType())
				.requestedAmount(dto.getRequestedAmount())
				.tenure(dto.getTenure())
				.purpose(dto.getPurpose())
				.applicationDate(dto.getApplicationDate() == null ? LocalDate.now() : dto.getApplicationDate())
				.status(LoanApplicationStatus.Submitted)
				.build();
		LoanApplication res = repo.save(e);
		log.info("Loan application {} submitted for customer {}", res.getApplicationId(), res.getCustomerId());
		return toDto(res);
	}

	@Override
	public LoanApplicationDto getById(Integer id) throws Exception {
		LoanApplication e = repo.findById(id).orElseThrow(() -> new Exception("LoanApplication not found with ID: " + id));
		return toDto(e);
	}

	@Override
	public List<LoanApplicationDto> getByCustomer(Integer customerId) {
		return toDtoList(repo.findByCustomerId(customerId));
	}

	@Override
	public List<LoanApplicationDto> getByStatus(LoanApplicationStatus status) {
		return toDtoList(repo.findByStatus(status));
	}

	@Override
	public LoanApplicationDto underReview(Integer id) throws Exception {
		return transition(id, LoanApplicationStatus.UnderReview);
	}

	@Override
	public LoanApplicationDto approveApplication(Integer id) throws Exception {
		return transition(id, LoanApplicationStatus.Approved);
	}

	@Override
	public LoanApplicationDto rejectApplication(Integer id) throws Exception {
		return transition(id, LoanApplicationStatus.Rejected);
	}

	private LoanApplicationDto transition(Integer id, LoanApplicationStatus target) throws Exception {
		LoanApplication e = repo.findById(id).orElseThrow(() -> new Exception("LoanApplication not found with ID: " + id));
		e.setStatus(target);
		LoanApplication res = repo.save(e);
		log.info("Loan application {} -> {}", id, target);
		return toDto(res);
	}

	private List<LoanApplicationDto> toDtoList(List<LoanApplication> list) {
		List<LoanApplicationDto> dtos = new ArrayList<>();
		for (LoanApplication e : list) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	private LoanApplicationDto toDto(LoanApplication e) {
		LoanApplicationDto dto = new LoanApplicationDto();
		dto.setApplicationId(e.getApplicationId());
		dto.setCustomerId(e.getCustomerId());
		dto.setLoanType(e.getLoanType());
		dto.setRequestedAmount(e.getRequestedAmount());
		dto.setTenure(e.getTenure());
		dto.setPurpose(e.getPurpose());
		dto.setApplicationDate(e.getApplicationDate());
		dto.setStatus(e.getStatus());
		return dto;
	}
}
