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

import com.project.dto.KycRecordDto;
import com.project.entities.KycRecord;
import com.project.enums.DocumentType;
import com.project.enums.KycStatus;
import com.project.repository.KycRecordRepository;

@ExtendWith(MockitoExtension.class)
class KycRecordServiceTest {

	@Mock
	KycRecordRepository repo;

	@InjectMocks
	KycRecordServiceImplementation service;

	@Test
	void submitKYC_valid() throws Exception {
		when(repo.save(any(KycRecord.class))).thenAnswer(inv -> {
			KycRecord k = inv.getArgument(0);
			if (k.getKycId() == null) {
				k.setKycId(1);
			}
			return k;
		});

		KycRecordDto dto = new KycRecordDto();
		dto.setCustomerId(101);
		dto.setDocumentType(DocumentType.Passport);
		dto.setDocumentNumber("P123");

		KycRecordDto result = service.submitKYC(dto);

		assertEquals(KycStatus.Pending, result.getStatus());
	}

	@Test
	void verifyKYC_pendingToVerified() throws Exception {
		KycRecord k = KycRecord.builder().kycId(5).customerId(101).status(KycStatus.Pending).build();
		when(repo.findById(5)).thenReturn(Optional.of(k));
		when(repo.save(any(KycRecord.class))).thenAnswer(inv -> inv.getArgument(0));

		KycRecordDto result = service.verifyKYC(5, 6);

		assertEquals(KycStatus.Verified, result.getStatus());
		assertEquals(6, result.getVerifiedBy());
	}

	@Test
	void rejectKYC_pendingToRejected() throws Exception {
		KycRecord k = KycRecord.builder().kycId(6).customerId(101).status(KycStatus.Pending).build();
		when(repo.findById(6)).thenReturn(Optional.of(k));
		when(repo.save(any(KycRecord.class))).thenAnswer(inv -> inv.getArgument(0));

		KycRecordDto result = service.rejectKYC(6);

		assertEquals(KycStatus.Rejected, result.getStatus());
	}
}
