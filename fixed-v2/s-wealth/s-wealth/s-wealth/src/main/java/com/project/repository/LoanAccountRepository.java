package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.LoanAccount;
import com.project.enums.LoanAccountStatus;

@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, Integer> {

	List<LoanAccount> findByCustomerId(Integer customerId);

	List<LoanAccount> findByStatus(LoanAccountStatus status);

	Optional<LoanAccount> findByApplication_ApplicationId(Integer applicationId);
}
