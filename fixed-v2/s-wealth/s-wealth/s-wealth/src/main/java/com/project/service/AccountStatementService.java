package com.project.service;

import java.time.LocalDate;
import java.util.List;

import com.project.dto.AccountStatementDto;

public interface AccountStatementService {

	AccountStatementDto generateStatement(Integer accountId, LocalDate from, LocalDate to) throws Exception;

	List<AccountStatementDto> getStatementsByAccount(Integer accountId);
}
