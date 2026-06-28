package com.project.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.dto.BankAccountDto;
import com.project.entities.BankAccount;
import com.project.enums.AccountStatus;
import com.project.enums.AccountType;
import com.project.repository.BankAccountRepository;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

	@Mock
	BankAccountRepository repo;

	@InjectMocks
	BankAccountServiceImplementation service;

	@Test
	void openAccount_valid() throws Exception {
		when(repo.existsByAccountNumber("AC100")).thenReturn(false);
		when(repo.save(any(BankAccount.class))).thenAnswer(inv -> {
			BankAccount a = inv.getArgument(0);
			if (a.getAccountId() == null) {
				a.setAccountId(1);
			}
			return a;
		});

		BankAccountDto dto = new BankAccountDto();
		dto.setCustomerId(101);
		dto.setAccountType(AccountType.Savings);
		dto.setAccountNumber("AC100");
		dto.setBalance(5000.0);

		BankAccountDto result = service.openAccount(dto);

		assertNotNull(result.getAccountId());
		assertEquals(AccountStatus.Active, result.getStatus());
	}

	@Test
	void getAccountByNumber_valid() throws Exception {
		BankAccount a = BankAccount.builder().accountId(1).accountNumber("AC100").balance(5000.0).build();
		when(repo.findByAccountNumber("AC100")).thenReturn(Optional.of(a));

		BankAccountDto result = service.getAccountByNumber("AC100");

		assertEquals("AC100", result.getAccountNumber());
	}

	@Test
	void getAccountByNumber_invalid_throwsException() {
		when(repo.findByAccountNumber("NOPE")).thenReturn(Optional.empty());
		assertThrows(Exception.class, () -> service.getAccountByNumber("NOPE"));
	}

	@Test
	void updateBalance_credit() throws Exception {
		BankAccount a = BankAccount.builder().accountId(1).balance(1000.0).build();
		when(repo.findById(1)).thenReturn(Optional.of(a));
		when(repo.save(any(BankAccount.class))).thenAnswer(inv -> inv.getArgument(0));

		BankAccountDto result = service.updateBalance(1, 500.0, true);

		assertEquals(1500.0, result.getBalance());
	}

	@Test
	void updateBalance_debit() throws Exception {
		BankAccount a = BankAccount.builder().accountId(1).balance(1000.0).build();
		when(repo.findById(1)).thenReturn(Optional.of(a));
		when(repo.save(any(BankAccount.class))).thenAnswer(inv -> inv.getArgument(0));

		BankAccountDto result = service.updateBalance(1, 400.0, false);

		assertEquals(600.0, result.getBalance());
	}
}
