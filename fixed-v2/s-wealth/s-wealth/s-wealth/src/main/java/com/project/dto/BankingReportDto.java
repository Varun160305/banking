package com.project.dto;

import java.time.LocalDateTime;

import com.project.enums.ReportScope;

public class BankingReportDto {

	private Integer reportId;
	private ReportScope scope;
	private String metrics;
	private LocalDateTime generatedDate;

	public BankingReportDto() {}

	public BankingReportDto(Integer reportId, ReportScope scope, String metrics, LocalDateTime generatedDate) {
		this.reportId = reportId;
		this.scope = scope;
		this.metrics = metrics;
		this.generatedDate = generatedDate;
	}

	public Integer getReportId() { return reportId; }
	public void setReportId(Integer reportId) { this.reportId = reportId; }

	public ReportScope getScope() { return scope; }
	public void setScope(ReportScope scope) { this.scope = scope; }

	public String getMetrics() { return metrics; }
	public void setMetrics(String metrics) { this.metrics = metrics; }

	public LocalDateTime getGeneratedDate() { return generatedDate; }
	public void setGeneratedDate(LocalDateTime generatedDate) { this.generatedDate = generatedDate; }
}
