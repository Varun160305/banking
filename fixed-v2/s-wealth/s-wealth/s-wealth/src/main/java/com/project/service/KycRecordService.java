package com.project.service;

import java.util.List;

import com.project.dto.KycRecordDto;

public interface KycRecordService {

	KycRecordDto submitKYC(KycRecordDto dto) throws Exception;

	KycRecordDto getById(Integer id) throws Exception;

	List<KycRecordDto> getKYCByCustomer(Integer customerId);

	KycRecordDto verifyKYC(Integer id, Integer verifiedBy) throws Exception;

	KycRecordDto rejectKYC(Integer id) throws Exception;
}
