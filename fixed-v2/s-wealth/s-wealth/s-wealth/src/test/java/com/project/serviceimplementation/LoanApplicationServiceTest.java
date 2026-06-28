package com.project.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.dto.LoanApplicationDto;
import com.project.entities.LoanApplication;
import com.project.enums.LoanApplicationStatus;
import com.project.enums.LoanType;
import com.project.repository.LoanApplicationRepository;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

	@Mock
	LoanApplicationRepository repo;

	@InjectMocks
	LoanApplicationServiceImplementation service;

	@Test
	void submitApplication_valid() throws Exception {
		when(repo.save(any(LoanApplication.class))).thenAnswer(inv -> {
			LoanApplication a = inv.getArgument(0);
			if (a.getApplicationId() == null) {
				a.setApplicationId(1);
			}
			return a;
		});

		LoanApplicationDto dto = new LoanApplicationDto();
		dto.setCustomerId(101);
		dto.setLoanType(LoanType.Home);
		dto.setRequestedAmount(1000000.0);
		dto.setTenure(60);

		LoanApplicationDto result = service.submitApplication(dto);

		assertNotNull(result.getApplicationId());
		assertEquals(LoanApplicationStatus.Submitted, result.getStatus());
	}

	@Test
	void approveApplication_underreview_to_approved() throws Exception {
		LoanApplication app = LoanApplication.builder().applicationId(5).customerId(101)
				.status(LoanApplicationStatus.UnderReview).build();
		when(repo.findById(5)).thenReturn(Optional.of(app));
		when(repo.save(any(LoanApplication.class))).thenAnswer(inv -> inv.getArgument(0));

		LoanApplicationDto result = service.approveApplication(5);

		assertEquals(LoanApplicationStatus.Approved, result.getStatus());
	}

	@Test
	void rejectApplication_underreview_to_rejected() throws Exception {
		LoanApplication app = LoanApplication.builder().applicationId(6).customerId(101)
				.status(LoanApplicationStatus.UnderReview).build();
		when(repo.findById(6)).thenReturn(Optional.of(app));
		when(repo.save(any(LoanApplication.class))).thenAnswer(inv -> inv.getArgument(0));

		LoanApplicationDto result = service.rejectApplication(6);

		assertEquals(LoanApplicationStatus.Rejected, result.getStatus());
	}

	@Test
	void getByCustomer_correct() {
		LoanApplication a1 = LoanApplication.builder().applicationId(1).customerId(101)
				.status(LoanApplicationStatus.Submitted).build();
		LoanApplication a2 = LoanApplication.builder().applicationId(2).customerId(101)
				.status(LoanApplicationStatus.Approved).build();
		when(repo.findByCustomerId(101)).thenReturn(List.of(a1, a2));

		List<LoanApplicationDto> result = service.getByCustomer(101);

		assertEquals(2, result.size());
	}
}
