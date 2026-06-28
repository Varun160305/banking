package com.project.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.dto.EmiScheduleDto;
import com.project.entities.EmiSchedule;
import com.project.entities.LoanAccount;
import com.project.enums.EmiStatus;
import com.project.repository.EmiScheduleRepository;
import com.project.repository.LoanAccountRepository;
import com.project.service.AuditTrailService;
import com.project.service.NotifierService;

@ExtendWith(MockitoExtension.class)
class EmiScheduleServiceTest {

	@Mock
	EmiScheduleRepository repo;

	@Mock
	LoanAccountRepository loanAccountRepo;

	@Mock
	NotifierService notifier;

	@Mock
	AuditTrailService auditTrail;

	@InjectMocks
	EmiScheduleServiceImplementation service;

	@Test
	void payEMI_pendingToPaid() throws Exception {
		LoanAccount loan = LoanAccount.builder().loanAccountId(1).outstandingBalance(100000.0).build();
		EmiSchedule emi = EmiSchedule.builder().emiId(10).loanAccount(loan).emiAmount(5000.0)
				.principal(4000.0).interest(1000.0).status(EmiStatus.Pending).build();
		when(repo.findById(10)).thenReturn(Optional.of(emi));
		when(repo.save(any(EmiSchedule.class))).thenAnswer(inv -> inv.getArgument(0));

		EmiScheduleDto result = service.payEMI(10);

		assertEquals(EmiStatus.Paid, result.getStatus());
		assertEquals(96000.0, loan.getOutstandingBalance());
	}

	@Test
	void payEMI_alreadyPaid_throwsException() {
		EmiSchedule emi = EmiSchedule.builder().emiId(11).status(EmiStatus.Paid).build();
		when(repo.findById(11)).thenReturn(Optional.of(emi));

		assertThrows(Exception.class, () -> service.payEMI(11));
	}

	@Test
	void getOverdueEMIs_returnsCorrect() {
		EmiSchedule e1 = EmiSchedule.builder().emiId(1).status(EmiStatus.Overdue).build();
		EmiSchedule e2 = EmiSchedule.builder().emiId(2).status(EmiStatus.Overdue).build();
		when(repo.findByStatus(EmiStatus.Overdue)).thenReturn(List.of(e1, e2));

		List<EmiScheduleDto> result = service.getOverdueEMIs();

		assertEquals(2, result.size());
	}
}
