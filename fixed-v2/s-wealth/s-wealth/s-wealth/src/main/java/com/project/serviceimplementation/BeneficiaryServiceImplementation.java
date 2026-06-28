package com.project.serviceimplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.BeneficiaryDto;
import com.project.entities.Beneficiary;
import com.project.enums.BeneficiaryStatus;
import com.project.repository.BeneficiaryRepository;
import com.project.service.BeneficiaryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BeneficiaryServiceImplementation implements BeneficiaryService {

	@Autowired
	BeneficiaryRepository repo;

	@Override
	public BeneficiaryDto addBeneficiary(BeneficiaryDto dto) throws Exception {
		if (repo.existsByCustomerIdAndAccountNumber(dto.getCustomerId(), dto.getAccountNumber())) {
			throw new Exception("This beneficiary account number is already registered for this customer.");
		}
		Beneficiary e = Beneficiary.builder()
				.customerId(dto.getCustomerId())
				.accountNumber(dto.getAccountNumber())
				.name(dto.getName())
				.bankName(dto.getBankName())
				.status(dto.getStatus() == null ? BeneficiaryStatus.Active : dto.getStatus())
				.build();
		Beneficiary res = repo.save(e);
		log.info("Beneficiary {} added for customer {}", res.getBeneficiaryId(), res.getCustomerId());
		return toDto(res);
	}

	@Override
	public List<BeneficiaryDto> getBeneficiariesByCustomer(Integer customerId) {
		List<BeneficiaryDto> dtos = new ArrayList<>();
		for (Beneficiary e : repo.findByCustomerIdAndStatus(customerId, BeneficiaryStatus.Active)) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	@Override
	public BeneficiaryDto getById(Integer id) throws Exception {
		Beneficiary e = repo.findById(id).orElseThrow(() -> new Exception("Beneficiary not found with ID: " + id));
		return toDto(e);
	}

	@Override
	public void deleteBeneficiary(Integer id) throws Exception {
		Beneficiary e = repo.findById(id).orElseThrow(() -> new Exception("Beneficiary not found with ID: " + id));
		// soft delete
		e.setStatus(BeneficiaryStatus.Deleted);
		repo.save(e);
		log.info("Beneficiary {} soft-deleted", id);
	}

	private BeneficiaryDto toDto(Beneficiary e) {
		BeneficiaryDto dto = new BeneficiaryDto();
		dto.setBeneficiaryId(e.getBeneficiaryId());
		dto.setCustomerId(e.getCustomerId());
		dto.setAccountNumber(e.getAccountNumber());
		dto.setName(e.getName());
		dto.setBankName(e.getBankName());
		dto.setStatus(e.getStatus());
		return dto;
	}
}
