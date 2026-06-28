package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.LoanApplication;
import com.project.enums.LoanApplicationStatus;
import com.project.enums.LoanType;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Integer> {

	List<LoanApplication> findByCustomerId(Integer customerId);

	List<LoanApplication> findByStatus(LoanApplicationStatus status);

	List<LoanApplication> findByLoanType(LoanType loanType);
}
