package com.project.serviceimplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ScheduledPaymentDto;
import com.project.entities.Beneficiary;
import com.project.entities.ScheduledPayment;
import com.project.enums.ScheduledPaymentStatus;
import com.project.repository.BeneficiaryRepository;
import com.project.repository.ScheduledPaymentRepository;
import com.project.service.ScheduledPaymentService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScheduledPaymentServiceImplementation implements ScheduledPaymentService {

	@Autowired
	ScheduledPaymentRepository repo;

	@Autowired
	BeneficiaryRepository beneficiaryRepo;

	@Override
	public ScheduledPaymentDto createSchedule(ScheduledPaymentDto dto) throws Exception {
		Beneficiary beneficiary = beneficiaryRepo.findById(dto.getBeneficiaryId())
				.orElseThrow(() -> new Exception("Beneficiary not found with ID: " + dto.getBeneficiaryId()));

		ScheduledPayment e = ScheduledPayment.builder()
				.customerId(dto.getCustomerId())
				.fromAccountId(dto.getFromAccountId())
				.beneficiary(beneficiary)
				.amount(dto.getAmount())
				.frequency(dto.getFrequency())
				.nextRunDate(dto.getNextRunDate())
				.status(dto.getStatus() == null ? ScheduledPaymentStatus.Active : dto.getStatus())
				.build();
		ScheduledPayment res = repo.save(e);
		log.info("Scheduled payment {} created for customer {}", res.getScheduleId(), res.getCustomerId());
		return toDto(res);
	}

	@Override
	public List<ScheduledPaymentDto> getSchedulesByCustomer(Integer customerId) {
		List<ScheduledPaymentDto> dtos = new ArrayList<>();
		for (ScheduledPayment e : repo.findByCustomerId(customerId)) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	@Override
	public ScheduledPaymentDto pauseSchedule(Integer id) throws Exception {
		ScheduledPayment e = repo.findById(id).orElseThrow(() -> new Exception("ScheduledPayment not found with ID: " + id));
		e.setStatus(ScheduledPaymentStatus.Paused);
		ScheduledPayment res = repo.save(e);
		log.info("Scheduled payment {} paused", id);
		return toDto(res);
	}

	@Override
	public ScheduledPaymentDto cancelSchedule(Integer id) throws Exception {
		ScheduledPayment e = repo.findById(id).orElseThrow(() -> new Exception("ScheduledPayment not found with ID: " + id));
		e.setStatus(ScheduledPaymentStatus.Cancelled);
		ScheduledPayment res = repo.save(e);
		log.info("Scheduled payment {} cancelled", id);
		return toDto(res);
	}

	@Override
	public List<ScheduledPaymentDto> getDuePayments(LocalDate date) {
		List<ScheduledPaymentDto> dtos = new ArrayList<>();
		for (ScheduledPayment e : repo.findByNextRunDateBefore(date)) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	private ScheduledPaymentDto toDto(ScheduledPayment e) {
		ScheduledPaymentDto dto = new ScheduledPaymentDto();
		dto.setScheduleId(e.getScheduleId());
		dto.setCustomerId(e.getCustomerId());
		dto.setFromAccountId(e.getFromAccountId());
		dto.setBeneficiaryId(e.getBeneficiary() == null ? null : e.getBeneficiary().getBeneficiaryId());
		dto.setAmount(e.getAmount());
		dto.setFrequency(e.getFrequency());
		dto.setNextRunDate(e.getNextRunDate());
		dto.setStatus(e.getStatus());
		return dto;
	}
}
