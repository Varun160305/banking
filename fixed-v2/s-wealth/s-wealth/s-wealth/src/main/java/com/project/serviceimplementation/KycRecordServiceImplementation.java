package com.project.serviceimplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.KycRecordDto;
import com.project.entities.KycRecord;
import com.project.enums.KycStatus;
import com.project.repository.KycRecordRepository;
import com.project.service.KycRecordService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KycRecordServiceImplementation implements KycRecordService {

	@Autowired
	KycRecordRepository repo;

	@Override
	public KycRecordDto submitKYC(KycRecordDto dto) throws Exception {
		KycRecord e = KycRecord.builder()
				.customerId(dto.getCustomerId())
				.documentType(dto.getDocumentType())
				.documentNumber(dto.getDocumentNumber())
				.status(KycStatus.Pending)
				.build();
		KycRecord res = repo.save(e);
		log.info("KYC {} submitted for customer {}", res.getKycId(), res.getCustomerId());
		return toDto(res);
	}

	@Override
	public KycRecordDto getById(Integer id) throws Exception {
		KycRecord e = repo.findById(id).orElseThrow(() -> new Exception("KycRecord not found with ID: " + id));
		return toDto(e);
	}

	@Override
	public List<KycRecordDto> getKYCByCustomer(Integer customerId) {
		List<KycRecordDto> dtos = new ArrayList<>();
		for (KycRecord e : repo.findByCustomerId(customerId)) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	@Override
	public KycRecordDto verifyKYC(Integer id, Integer verifiedBy) throws Exception {
		KycRecord e = repo.findById(id).orElseThrow(() -> new Exception("KycRecord not found with ID: " + id));
		e.setStatus(KycStatus.Verified);
		e.setVerifiedBy(verifiedBy);
		e.setVerificationDate(LocalDate.now());
		KycRecord res = repo.save(e);
		log.info("KYC {} verified by {}", id, verifiedBy);
		return toDto(res);
	}

	@Override
	public KycRecordDto rejectKYC(Integer id) throws Exception {
		KycRecord e = repo.findById(id).orElseThrow(() -> new Exception("KycRecord not found with ID: " + id));
		e.setStatus(KycStatus.Rejected);
		KycRecord res = repo.save(e);
		log.info("KYC {} rejected", id);
		return toDto(res);
	}

	private KycRecordDto toDto(KycRecord e) {
		KycRecordDto dto = new KycRecordDto();
		dto.setKycId(e.getKycId());
		dto.setCustomerId(e.getCustomerId());
		dto.setDocumentType(e.getDocumentType());
		dto.setDocumentNumber(e.getDocumentNumber());
		dto.setVerifiedBy(e.getVerifiedBy());
		dto.setVerificationDate(e.getVerificationDate());
		dto.setStatus(e.getStatus());
		return dto;
	}
}
