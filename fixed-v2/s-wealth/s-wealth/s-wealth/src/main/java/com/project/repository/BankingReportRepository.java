package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.BankingReport;
import com.project.enums.ReportScope;

@Repository
public interface BankingReportRepository extends JpaRepository<BankingReport, Integer> {

	List<BankingReport> findByScope(ReportScope scope);
}
