package com.project.serviceimplementation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.BankingReportDto;
import com.project.entities.BankAccount;
import com.project.entities.BankingReport;
import com.project.entities.LoanAccount;
import com.project.enums.EmiStatus;
import com.project.enums.LoanAccountStatus;
import com.project.enums.ReportScope;
import com.project.repository.BankAccountRepository;
import com.project.repository.BankingReportRepository;
import com.project.repository.EmiScheduleRepository;
import com.project.repository.LoanAccountRepository;
import com.project.service.BankingReportService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankingReportServiceImplementation implements BankingReportService {

	@Autowired
	BankingReportRepository repo;

	@Autowired
	BankAccountRepository accountRepo;

	@Autowired
	LoanAccountRepository loanAccountRepo;

	@Autowired
	EmiScheduleRepository emiRepo;

	@Override
	public BankingReportDto generateReport(ReportScope scope) throws Exception {
		Map<String, Object> metrics = computeMetrics();
		BankingReport e = BankingReport.builder()
				.scope(scope == null ? ReportScope.Branch : scope)
				.metrics(toJson(metrics))
				.build();
		BankingReport res = repo.save(e);
		log.info("Banking report {} generated (scope={})", res.getReportId(), res.getScope());
		return toDto(res);
	}

	@Override
	public List<BankingReportDto> getAllReports() {
		return toDtoList(repo.findAll());
	}

	@Override
	public List<BankingReportDto> getByScope(ReportScope scope) {
		return toDtoList(repo.findByScope(scope));
	}

	@Override
	public BankingReportDto getReportById(Integer id) throws Exception {
		BankingReport e = repo.findById(id).orElseThrow(() -> new Exception("BankingReport not found with ID: " + id));
		return toDto(e);
	}

	@Override
	public Map<String, Object> getSummary() {
		return computeMetrics();
	}

	/** Live, DB-backed metrics computed across the Account / Loan / EMI tables (per the PDF). */
	private Map<String, Object> computeMetrics() {
		double totalDeposits = 0;
		long newAccountsOpened = 0;
		for (BankAccount a : accountRepo.findAll()) {
			totalDeposits += (a.getBalance() == null ? 0 : a.getBalance());
			newAccountsOpened++;
		}

		double loanBookValue = 0;
		long totalLoans = 0;
		long npaLoans = 0;
		for (LoanAccount l : loanAccountRepo.findAll()) {
			loanBookValue += (l.getOutstandingBalance() == null ? 0 : l.getOutstandingBalance());
			totalLoans++;
			if (l.getStatus() == LoanAccountStatus.NPA) {
				npaLoans++;
			}
		}
		double npaRatio = totalLoans == 0 ? 0.0 : round2((npaLoans * 100.0) / totalLoans);

		long totalEmis = emiRepo.count();
		long paidEmis = emiRepo.findByStatus(EmiStatus.Paid).size();
		double emiCollectionRate = totalEmis == 0 ? 0.0 : round2((paidEmis * 100.0) / totalEmis);

		Map<String, Object> m = new LinkedHashMap<>();
		m.put("totalDeposits", round2(totalDeposits));
		m.put("loanBookValue", round2(loanBookValue));
		m.put("npaRatio", npaRatio);
		m.put("emiCollectionRate", emiCollectionRate);
		m.put("newAccountsOpened", newAccountsOpened);
		return m;
	}

	private double round2(double x) {
		return Math.round(x * 100.0) / 100.0;
	}

	private String toJson(Map<String, Object> m) {
		StringBuilder sb = new StringBuilder("{");
		boolean first = true;
		for (Map.Entry<String, Object> en : m.entrySet()) {
			if (!first) {
				sb.append(",");
			}
			sb.append("\"").append(en.getKey()).append("\":").append(en.getValue());
			first = false;
		}
		return sb.append("}").toString();
	}

	private List<BankingReportDto> toDtoList(List<BankingReport> list) {
		List<BankingReportDto> dtos = new ArrayList<>();
		for (BankingReport e : list) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	private BankingReportDto toDto(BankingReport e) {
		BankingReportDto dto = new BankingReportDto();
		dto.setReportId(e.getReportId());
		dto.setScope(e.getScope());
		dto.setMetrics(e.getMetrics());
		dto.setGeneratedDate(e.getGeneratedDate());
		return dto;
	}
}
