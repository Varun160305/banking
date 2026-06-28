package com.project.service;

import java.util.List;

import com.project.dto.BeneficiaryDto;

public interface BeneficiaryService {

	BeneficiaryDto addBeneficiary(BeneficiaryDto dto) throws Exception;

	List<BeneficiaryDto> getBeneficiariesByCustomer(Integer customerId);

	BeneficiaryDto getById(Integer id) throws Exception;

	void deleteBeneficiary(Integer id) throws Exception;
}
