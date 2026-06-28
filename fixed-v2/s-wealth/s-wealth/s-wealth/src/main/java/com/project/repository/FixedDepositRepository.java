package com.project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.FixedDeposit;
import com.project.enums.FixedDepositStatus;

@Repository
public interface FixedDepositRepository extends JpaRepository<FixedDeposit, Integer> {

	List<FixedDeposit> findByCustomerId(Integer customerId);

	List<FixedDeposit> findByAccountId(Integer accountId);

	List<FixedDeposit> findByStatus(FixedDepositStatus status);

	List<FixedDeposit> findByMaturityDateBefore(LocalDate date);
}
