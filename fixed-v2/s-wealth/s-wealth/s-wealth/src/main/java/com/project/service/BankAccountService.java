package com.project.service;

import java.util.List;

import com.project.dto.BankAccountDto;

public interface BankAccountService {

	BankAccountDto openAccount(BankAccountDto dto) throws Exception;

	BankAccountDto getById(Integer id) throws Exception;

	BankAccountDto getAccountByNumber(String accountNumber) throws Exception;

	List<BankAccountDto> getAccountsByCustomer(Integer customerId);

	BankAccountDto updateBalance(Integer accountId, double amount, boolean credit) throws Exception;

	BankAccountDto closeAccount(Integer accountId) throws Exception;

	BankAccountDto deposit(Integer accountId, double amount) throws Exception;
}
