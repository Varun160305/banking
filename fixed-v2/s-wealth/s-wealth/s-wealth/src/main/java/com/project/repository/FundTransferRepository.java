package com.project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.entities.BankAccount;
import com.project.entities.FundTransfer;
import com.project.enums.TransferStatus;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Integer> {

    // ✅ Get transfers by sender account
    List<FundTransfer> findByFromAccount(BankAccount fromAccount);

    // ✅ Get transfers by receiver account
    List<FundTransfer> findByToAccount(BankAccount toAccount);

    // ✅ Filter by status
    List<FundTransfer> findByStatus(TransferStatus status);

    // ✅ Both incoming & outgoing transfers
    List<FundTransfer> findByFromAccountOrToAccount(BankAccount fromAccount, BankAccount toAccount);

    // ✅ Statement (date-based query)
    @Query("SELECT f FROM FundTransfer f " +
           "WHERE (f.fromAccount = :account OR f.toAccount = :account) " +
           "AND f.transferDate BETWEEN :from AND :to")
    List<FundTransfer> findStatement(
            @Param("account") BankAccount account,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}