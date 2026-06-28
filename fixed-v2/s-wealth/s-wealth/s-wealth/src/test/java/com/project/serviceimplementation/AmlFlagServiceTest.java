package com.project.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.dto.AmlFlagDto;
import com.project.entities.AmlFlag;
import com.project.entities.BankAccount;
import com.project.enums.AmlStatus;
import com.project.enums.FlagType;
import com.project.enums.Severity;
import com.project.repository.AmlFlagRepository;
import com.project.repository.BankAccountRepository;

@ExtendWith(MockitoExtension.class)
class AmlFlagServiceTest {

	@Mock
	AmlFlagRepository repo;

	@Mock
	BankAccountRepository accountRepo;

	@InjectMocks
	AmlFlagServiceImplementation service;

	@Test
	void raiseFlag_valid() throws Exception {
		BankAccount acc = BankAccount.builder().accountId(1).build();
		when(accountRepo.findById(1)).thenReturn(Optional.of(acc));
		when(repo.save(any(AmlFlag.class))).thenAnswer(inv -> {
			AmlFlag f = inv.getArgument(0);
			if (f.getFlagId() == null) {
				f.setFlagId(1);
			}
			return f;
		});

		AmlFlagDto dto = new AmlFlagDto();
		dto.setAccountId(1);
		dto.setTransactionId(10);
		dto.setFlagType(FlagType.LargeTransaction);
		dto.setSeverity(Severity.High);

		AmlFlagDto result = service.raiseFlag(dto);

		assertEquals(AmlStatus.Open, result.getStatus());
		assertEquals(1, result.getAccountId());
	}

	@Test
	void clearFlag_investigatedToCleared() throws Exception {
		AmlFlag f = AmlFlag.builder().flagId(2).status(AmlStatus.Investigated).build();
		when(repo.findById(2)).thenReturn(Optional.of(f));
		when(repo.save(any(AmlFlag.class))).thenAnswer(inv -> inv.getArgument(0));

		AmlFlagDto result = service.clearFlag(2);

		assertEquals(AmlStatus.Cleared, result.getStatus());
	}
}
