package com.project.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.project.dto.FundTransferDto;
import com.project.entities.BankAccount;
import com.project.entities.FundTransfer;
import com.project.entities.User;
import com.project.enums.KycStatus;
import com.project.enums.TransferStatus;
import com.project.enums.TransferType;
import com.project.repository.AmlFlagRepository;
import com.project.repository.BankAccountRepository;
import com.project.repository.FundTransferRepository;
import com.project.repository.KycRecordRepository;
import com.project.repository.UserRepository;
import com.project.service.AuditTrailService;
import com.project.service.LedgerService;
import com.project.service.NotifierService;

@ExtendWith(MockitoExtension.class)
class FundTransferServiceTest {

    @Mock
    FundTransferRepository repo;

    @Mock
    BankAccountRepository accountRepo;

    @Mock
    KycRecordRepository kycRepo;

    @Mock
    AmlFlagRepository amlRepo;

    @Mock
    LedgerService ledger;

    @Mock
    NotifierService notifier;

    @Mock
    AuditTrailService auditTrail;

    @Mock
    UserRepository userRepo;  // ✅ NEW

    @InjectMocks
    FundTransferServiceImplementation service;

    @Test
    void initiateTransfer_valid_completedStatus() throws Exception {

        // ✅ Mock logged-in user
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("test@mail.com", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = User.builder()
                .userId(101)
                .email("test@mail.com")
                .build();

        when(userRepo.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        BankAccount from = BankAccount.builder()
                .accountId(1)
                .customerId(101) // ✅ matches user
                .balance(1000.0)
                .build();

        BankAccount to = BankAccount.builder()
                .accountId(2)
                .customerId(102)
                .balance(500.0)
                .build();

        when(accountRepo.findById(1)).thenReturn(Optional.of(from));
        when(accountRepo.findById(2)).thenReturn(Optional.of(to));

        when(kycRepo.existsByCustomerIdAndStatus(eq(101), eq(KycStatus.Verified))).thenReturn(true);

        when(repo.save(any(FundTransfer.class))).thenAnswer(inv -> {
            FundTransfer f = inv.getArgument(0);
            if (f.getTransferId() == null) {
                f.setTransferId(1);
            }
            return f;
        });

        FundTransferDto result = service.initiateTransfer(
                1, 2, 100.0, TransferType.Internal, "Test transfer"  // ✅ added remarks
        );

        assertEquals(TransferStatus.Completed, result.getStatus());
    }

    @Test
    void initiateTransfer_sameAccount_throwsException() {

        assertThrows(Exception.class,
                () -> service.initiateTransfer(
                        1, 1, 100.0, TransferType.Internal, "Test"
                ));
    }

}