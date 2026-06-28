package com.project.service;

import java.util.List;
import java.util.Map;

import com.project.dto.BankingReportDto;
import com.project.enums.ReportScope;

public interface BankingReportService {

	BankingReportDto generateReport(ReportScope scope) throws Exception;

	List<BankingReportDto> getAllReports();

	List<BankingReportDto> getByScope(ReportScope scope);

	BankingReportDto getReportById(Integer id) throws Exception;

	Map<String, Object> getSummary();
}
