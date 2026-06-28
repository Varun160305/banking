package com.project.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.AccountStatement;

@Repository
public interface AccountStatementRepository extends JpaRepository<AccountStatement, Integer> {

	List<AccountStatement> findByBankAccount_AccountId(Integer accountId);

	List<AccountStatement> findByPeriodStartBetween(LocalDate from, LocalDate to);

	// used by the LedgerService daily upsert
	Optional<AccountStatement> findByBankAccount_AccountIdAndPeriodStartAndPeriodEnd(Integer accountId,
			LocalDate periodStart, LocalDate periodEnd);
}
