package com.project.service;

import java.time.LocalDate;
import java.util.List;

import com.project.dto.FundTransferDto;
import com.project.enums.TransferType;

public interface FundTransferService {

	FundTransferDto initiateTransfer(Integer fromAccountId, Integer toAccountId, Double amount, TransferType type,String remarks) throws Exception;

	FundTransferDto reverseTransfer(Integer id) throws Exception;

	FundTransferDto getById(Integer id) throws Exception;

	List<FundTransferDto> getByFromAccountId(Integer fromAccountId);

	List<FundTransferDto> getByToAccountId(Integer toAccountId);

	List<FundTransferDto> getTransfersByAccount(Integer accountId);

	List<FundTransferDto> getStatement(Integer accountId, LocalDate from, LocalDate to);
}
