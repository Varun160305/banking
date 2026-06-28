package com.project.serviceimplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.FundTransferDto;
import com.project.entities.*;
import com.project.enums.*;
import com.project.repository.*;
import com.project.service.*;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FundTransferServiceImplementation implements FundTransferService {

    private static final double LARGE_TXN_THRESHOLD = 100000.0;

    @Autowired
    FundTransferRepository repo;

    @Autowired
    BankAccountRepository accountRepo;

    @Autowired
    KycRecordRepository kycRepo;

    @Autowired
    AmlFlagRepository amlRepo;

    @Autowired
    LedgerService ledger;

    @Autowired
    NotifierService notifier;

    @Autowired
    AuditTrailService auditTrail;
    

@Autowired
UserRepository userRepo;


    @Override
    @Transactional
    public FundTransferDto initiateTransfer(Integer fromAccountId, Integer toAccountId,
                                            Double amount, TransferType type, String remarks)
            throws Exception {

        if (fromAccountId == null || toAccountId == null) {
            throw new Exception("Both account IDs are required.");
        }

        if (fromAccountId.equals(toAccountId)) {
            throw new Exception("Cannot transfer to same account.");
        }

        if (amount == null || amount <= 0) {
            throw new Exception("Invalid amount.");
        }

        // ✅ ✅ STEP 1: Get logged-in user
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new Exception("Authenticated user not found"));

        // ✅ Fetch accounts
        BankAccount from = accountRepo.findById(fromAccountId)
                .orElseThrow(() -> new Exception("Sender not found"));

        BankAccount to = accountRepo.findById(toAccountId)
                .orElseThrow(() -> new Exception("Receiver not found"));

        // ✅ ✅ STEP 2: Ownership validation (VERY IMPORTANT 🔥)
        if (!from.getCustomerId().equals(user.getUserId())) {
            throw new Exception("Unauthorized: You can only transfer from your own account");
        }

        // ✅ Balance check
        if (from.getBalance() < amount) {
            throw new Exception("Insufficient balance");
        }

        // ✅ Create transaction
        FundTransfer e = FundTransfer.builder()
                .fromAccount(from)
                .toAccount(to)
                .amount(amount)
                .currency(Currency.INR)
                .transferType(type == null ? TransferType.Internal : type)
                .remarks(remarks)
                .transferDate(LocalDate.now())
                .status(TransferStatus.Initiated)
                .build();

        e = repo.save(e);

        // ✅ Ledger updates
        ledger.debit(fromAccountId, amount);
        ledger.credit(toAccountId, amount);

        e.setStatus(TransferStatus.Completed);
        e = repo.save(e);

        log.info("Transfer {} completed: {} -> {} amount {}", e.getTransferId(), fromAccountId, toAccountId, amount);

        // ✅ AML check
        if (amount >= LARGE_TXN_THRESHOLD) {
            amlRepo.save(AmlFlag.builder()
                    .bankAccount(from)
                    .transactionId(e.getTransferId())
                    .flagType(FlagType.LargeTransaction)
                    .severity(Severity.High)
                    .raisedDate(LocalDate.now())
                    .status(AmlStatus.Open)
                    .build());
        }

        // ✅ Notifications
        notifier.notify(from.getCustomerId(),
                "Debited " + amount + " (transfer #" + e.getTransferId() + ")",
                NotificationCategory.Transaction);

        notifier.notify(to.getCustomerId(),
                "Credited " + amount + " (transfer #" + e.getTransferId() + ")",
                NotificationCategory.Transaction);

        auditTrail.record(AuditAction.TRANSFER, AuditModule.TRANSFER_MODULE);

        return toDto(e);
    }
  

    @Override
    @Transactional
    public FundTransferDto reverseTransfer(Integer id) throws Exception {

        FundTransfer e = repo.findById(id)
                .orElseThrow(() -> new Exception("Transfer not found"));

        if (e.getStatus() != TransferStatus.Completed) {
            throw new Exception("Only completed transfers can be reversed");
        }

        ledger.credit(e.getFromAccount().getAccountId(), e.getAmount());
        ledger.debit(e.getToAccount().getAccountId(), e.getAmount());

        e.setStatus(TransferStatus.Reversed);
        e = repo.save(e);

        BankAccount from = e.getFromAccount();
        BankAccount to = e.getToAccount();

        if (from != null) {
            notifier.notify(from.getCustomerId(),
                    "Transfer reversed. Amount credited back: " + e.getAmount(),
                    NotificationCategory.Transaction);
        }

        if (to != null) {
            notifier.notify(to.getCustomerId(),
                    "Transfer reversed. Amount debited: " + e.getAmount(),
                    NotificationCategory.Transaction);
        }

        auditTrail.record(AuditAction.TRANSFER, AuditModule.TRANSFER_MODULE);

        log.info("Transfer {} reversed", id);

        return toDto(e);
    }

    @Override
    public FundTransferDto getById(Integer id) throws Exception {
        FundTransfer e = repo.findById(id)
                .orElseThrow(() -> new Exception("Transfer not found"));
        return toDto(e);
    }

    @Override
    public List<FundTransferDto> getByFromAccountId(Integer fromAccountId) {

        if (fromAccountId == null) {
            throw new RuntimeException("fromAccountId is required");
        }

        BankAccount acc = accountRepo.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return toDtoList(repo.findByFromAccount(acc));
    }

    @Override
    public List<FundTransferDto> getByToAccountId(Integer toAccountId) {

        if (toAccountId == null) {
            throw new RuntimeException("toAccountId is required");
        }

        BankAccount acc = accountRepo.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return toDtoList(repo.findByToAccount(acc));
    }
    @Override
    public List<FundTransferDto> getTransfersByAccount(Integer accountId) {

        if (accountId == null) {
            throw new RuntimeException("accountId is required");
        }

        BankAccount acc = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return toDtoList(repo.findByFromAccountOrToAccount(acc, acc));
    }

    @Override
    public List<FundTransferDto> getStatement(Integer accountId, LocalDate from, LocalDate to) {

        if (accountId == null) {
            throw new RuntimeException("accountId is required");
        }

        if (from == null || to == null) {
            throw new RuntimeException("Date range is required");
        }

        if (from.isAfter(to)) {
            throw new RuntimeException("Invalid date range");
        }

        BankAccount acc = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return toDtoList(repo.findStatement(acc, from, to));
    }

    private List<FundTransferDto> toDtoList(List<FundTransfer> list) {
        List<FundTransferDto> dtos = new ArrayList<>();
        for (FundTransfer e : list) {
            dtos.add(toDto(e));
        }
        return dtos;
    }

    private FundTransferDto toDto(FundTransfer e) {
        FundTransferDto dto = new FundTransferDto();

        dto.setTransferId(e.getTransferId());
        dto.setFromAccountId(e.getFromAccount().getAccountId());
        dto.setToAccountId(e.getToAccount().getAccountId());
        dto.setAmount(e.getAmount());
        dto.setCurrency(e.getCurrency());
        dto.setTransferType(e.getTransferType());
        dto.setRemarks(e.getRemarks());
        dto.setTransferDate(e.getTransferDate());
        dto.setStatus(e.getStatus());

        return dto;
    }

	
}
