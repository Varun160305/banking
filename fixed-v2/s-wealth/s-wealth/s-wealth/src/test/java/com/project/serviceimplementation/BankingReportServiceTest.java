package com.project.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.dto.BankingReportDto;
import com.project.entities.BankingReport;
import com.project.enums.EmiStatus;
import com.project.enums.ReportScope;
import com.project.repository.BankAccountRepository;
import com.project.repository.BankingReportRepository;
import com.project.repository.EmiScheduleRepository;
import com.project.repository.LoanAccountRepository;

@ExtendWith(MockitoExtension.class)
class BankingReportServiceTest {

	@Mock
	BankingReportRepository repo;
	@Mock
	BankAccountRepository accountRepo;
	@Mock
	LoanAccountRepository loanAccountRepo;
	@Mock
	EmiScheduleRepository emiRepo;

	@InjectMocks
	BankingReportServiceImplementation service;

	@Test
	void generateReport_hasMetrics() throws Exception {
		when(accountRepo.findAll()).thenReturn(List.of());
		when(loanAccountRepo.findAll()).thenReturn(List.of());
		when(emiRepo.count()).thenReturn(0L);
		when(emiRepo.findByStatus(EmiStatus.Paid)).thenReturn(List.of());
		when(repo.save(any(BankingReport.class))).thenAnswer(inv -> {
			BankingReport r = inv.getArgument(0);
			if (r.getReportId() == null) {
				r.setReportId(1);
			}
			return r;
		});

		BankingReportDto result = service.generateReport(ReportScope.Branch);

		assertNotNull(result.getMetrics());
		assertTrue(result.getMetrics().contains("totalDeposits"));
	}

	@Test
	void getReportById_invalid_throwsException() {
		when(repo.findById(999)).thenReturn(Optional.empty());
		assertThrows(Exception.class, () -> service.getReportById(999));
	}
}
