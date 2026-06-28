package com.project.serviceimplementation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.EmiScheduleDto;
import com.project.entities.EmiSchedule;
import com.project.entities.LoanAccount;
import com.project.enums.AuditAction;
import com.project.enums.AuditModule;
import com.project.enums.EmiStatus;
import com.project.enums.NotificationCategory;
import com.project.repository.EmiScheduleRepository;
import com.project.repository.LoanAccountRepository;
import com.project.service.AuditTrailService;
import com.project.service.EmiScheduleService;
import com.project.service.NotifierService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmiScheduleServiceImplementation implements EmiScheduleService {

	@Autowired
	EmiScheduleRepository repo;

	@Autowired
	LoanAccountRepository loanAccountRepo;

	@Autowired
	NotifierService notifier;

	@Autowired
	AuditTrailService auditTrail;

	@Override
	@Transactional
	public EmiScheduleDto payEMI(Integer emiId) throws Exception {
		EmiSchedule e = repo.findById(emiId).orElseThrow(() -> new Exception("EmiSchedule not found with ID: " + emiId));
		if (e.getStatus() == EmiStatus.Paid) {
			throw new Exception("EMI " + emiId + " is already PAID.");
		}
		e.setStatus(EmiStatus.Paid);
		EmiSchedule res = repo.save(e);

		// reduce the loan account's outstanding balance by the principal component
		LoanAccount loan = e.getLoanAccount();
		if (loan != null) {
			double outstanding = (loan.getOutstandingBalance() == null ? 0.0 : loan.getOutstandingBalance())
					- (e.getPrincipal() == null ? 0.0 : e.getPrincipal());
			if (outstanding < 0) {
				outstanding = 0;
			}
			loan.setOutstandingBalance(outstanding);
			loanAccountRepo.save(loan);
			log.info("EMI {} paid; loan account {} outstanding now {}", emiId, loan.getLoanAccountId(), outstanding);
			// cross-module: notify the customer (Notifications module)
			notifier.notify(loan.getCustomerId(), "EMI #" + emiId + " paid; outstanding balance now " + outstanding + ".",
					NotificationCategory.Loan);
		}
		// cross-module: audit trail (IAM module)
		auditTrail.record(AuditAction.UPDATE, AuditModule.LOAN_MODULE);
		return toDto(res);
	}

	@Override
	public List<EmiScheduleDto> getOverdueEMIs() {
		return toDtoList(repo.findByStatus(EmiStatus.Overdue));
	}

	@Override
	public List<EmiScheduleDto> getScheduleByLoanAccount(Integer loanAccountId) {
		return toDtoList(repo.findByLoanAccount_LoanAccountId(loanAccountId));
	}

	@Override
	public Map<String, Object> getSummary(Integer loanAccountId) throws Exception {
		List<EmiSchedule> list = repo.findByLoanAccount_LoanAccountId(loanAccountId);
		long paid = list.stream().filter(e -> e.getStatus() == EmiStatus.Paid).count();
		long pending = list.stream().filter(e -> e.getStatus() == EmiStatus.Pending).count();
		long overdue = list.stream().filter(e -> e.getStatus() == EmiStatus.Overdue).count();
		Double outstanding = loanAccountRepo.findById(loanAccountId)
				.map(LoanAccount::getOutstandingBalance).orElse(null);

		Map<String, Object> summary = new LinkedHashMap<>();
		summary.put("totalEMIs", list.size());
		summary.put("paid", paid);
		summary.put("pending", pending);
		summary.put("overdue", overdue);
		summary.put("outstanding", outstanding);
		return summary;
	}

	private List<EmiScheduleDto> toDtoList(List<EmiSchedule> list) {
		List<EmiScheduleDto> dtos = new ArrayList<>();
		for (EmiSchedule e : list) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	private EmiScheduleDto toDto(EmiSchedule e) {
		EmiScheduleDto dto = new EmiScheduleDto();
		dto.setEmiId(e.getEmiId());
		dto.setLoanAccountId(e.getLoanAccount() == null ? null : e.getLoanAccount().getLoanAccountId());
		dto.setDueDate(e.getDueDate());
		dto.setEmiAmount(e.getEmiAmount());
		dto.setPrincipal(e.getPrincipal());
		dto.setInterest(e.getInterest());
		dto.setStatus(e.getStatus());
		return dto;
	}
}
