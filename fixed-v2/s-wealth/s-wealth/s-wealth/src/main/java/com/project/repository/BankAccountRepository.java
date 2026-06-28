package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.BankAccount;
import com.project.enums.AccountStatus;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

	boolean existsByAccountNumber(String accountNumber);

	List<BankAccount> findByCustomerId(Integer customerId);

	Optional<BankAccount> findByAccountNumber(String accountNumber);

	List<BankAccount> findByStatus(AccountStatus status);
}
