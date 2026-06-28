package com.project.service;

import java.util.List;

import com.project.dto.LoanApplicationDto;
import com.project.enums.LoanApplicationStatus;

public interface LoanApplicationService {

	LoanApplicationDto submitApplication(LoanApplicationDto dto) throws Exception;

	LoanApplicationDto getById(Integer id) throws Exception;

	List<LoanApplicationDto> getByCustomer(Integer customerId);

	List<LoanApplicationDto> getByStatus(LoanApplicationStatus status);

	LoanApplicationDto underReview(Integer id) throws Exception;

	LoanApplicationDto approveApplication(Integer id) throws Exception;

	LoanApplicationDto rejectApplication(Integer id) throws Exception;
}
