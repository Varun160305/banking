package com.project.service;

import java.util.List;

import com.project.dto.LoanAccountDto;

public interface LoanAccountService {

	LoanAccountDto disburseLoan(Integer applicationId) throws Exception;

	LoanAccountDto getById(Integer id) throws Exception;

	List<LoanAccountDto> getByCustomer(Integer customerId);

	LoanAccountDto markNPA(Integer loanAccountId) throws Exception;

	LoanAccountDto closeLoan(Integer loanAccountId) throws Exception;
}
