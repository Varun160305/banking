package com.project.service;

import java.time.LocalDate;
import java.util.List;

import com.project.dto.FixedDepositDto;

public interface FixedDepositService {

	FixedDepositDto createFD(FixedDepositDto dto) throws Exception;

	FixedDepositDto getById(Integer id) throws Exception;

	List<FixedDepositDto> getByCustomer(Integer customerId);

	FixedDepositDto matureFD(Integer id) throws Exception;

	FixedDepositDto closeFD(Integer id) throws Exception;

	List<FixedDepositDto> getMaturedFDs();

	List<FixedDepositDto> getMaturing(LocalDate before);
}
