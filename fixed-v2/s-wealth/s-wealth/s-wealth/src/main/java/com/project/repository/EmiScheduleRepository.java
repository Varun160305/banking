package com.project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.EmiSchedule;
import com.project.enums.EmiStatus;

@Repository
public interface EmiScheduleRepository extends JpaRepository<EmiSchedule, Integer> {

	List<EmiSchedule> findByLoanAccount_LoanAccountId(Integer loanAccountId);

	List<EmiSchedule> findByStatus(EmiStatus status);

	List<EmiSchedule> findByDueDateBefore(LocalDate date);
}
