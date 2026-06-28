package com.project.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.dto.FixedDepositDto;
import com.project.entities.BankAccount;
import com.project.entities.FixedDeposit;
import com.project.enums.FixedDepositStatus;
import com.project.repository.FixedDepositRepository;
import com.project.service.AuditTrailService;
import com.project.service.LedgerService;
import com.project.service.NotifierService;

@ExtendWith(MockitoExtension.class)
class FixedDepositServiceTest {

	@Mock
	FixedDepositRepository repo;
	@Mock
	LedgerService ledger;
	@Mock
	NotifierService notifier;
	@Mock
	AuditTrailService auditTrail;

	@InjectMocks
	FixedDepositServiceImplementation service;

	@Test
	void createFD_valid() throws Exception {
		when(ledger.debit(anyInt(), anyDouble())).thenReturn(BankAccount.builder().accountId(1).customerId(101).build());
		when(repo.save(any(FixedDeposit.class))).thenAnswer(inv -> {
			FixedDeposit f = inv.getArgument(0);
			if (f.getFdId() == null) {
				f.setFdId(1);
			}
			return f;
		});

		FixedDepositDto dto = new FixedDepositDto();
		dto.setCustomerId(101);
		dto.setAccountId(1);
		dto.setPrincipal(100000.0);
		dto.setInterestRate(7.0);
		dto.setTenure(12);

		FixedDepositDto result = service.createFD(dto);

		assertEquals(FixedDepositStatus.Active, result.getStatus());
	}

	@Test
	void matureFD_activeToMatured() throws Exception {
		FixedDeposit fd = FixedDeposit.builder().fdId(5).accountId(1).status(FixedDepositStatus.Active).build();
		when(repo.findById(5)).thenReturn(Optional.of(fd));
		when(repo.save(any(FixedDeposit.class))).thenAnswer(inv -> inv.getArgument(0));

		FixedDepositDto result = service.matureFD(5);

		assertEquals(FixedDepositStatus.Matured, result.getStatus());
	}

	@Test
	void matureFD_alreadyMatured_throwsException() {
		FixedDeposit fd = FixedDeposit.builder().fdId(6).status(FixedDepositStatus.Matured).build();
		when(repo.findById(6)).thenReturn(Optional.of(fd));

		assertThrows(Exception.class, () -> service.matureFD(6));
	}
}
