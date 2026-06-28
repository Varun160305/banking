package com.project.serviceimplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.LoanAccountDto;
import com.project.entities.BankAccount;
import com.project.entities.EmiSchedule;
import com.project.entities.LoanAccount;
import com.project.entities.LoanApplication;
import com.project.enums.AuditAction;
import com.project.enums.AuditModule;
import com.project.enums.EmiStatus;
import com.project.enums.LoanAccountStatus;
import com.project.enums.LoanApplicationStatus;
import com.project.enums.NotificationCategory;
import com.project.repository.BankAccountRepository;
import com.project.repository.EmiScheduleRepository;
import com.project.repository.LoanAccountRepository;
import com.project.repository.LoanApplicationRepository;
import com.project.service.AuditTrailService;
import com.project.service.LedgerService;
import com.project.service.LoanAccountService;
import com.project.service.NotifierService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoanAccountServiceImplementation implements LoanAccountService {

	/** Default annual interest rate applied at disbursal (cross-module rate tables not modelled here). */
	private static final double DEFAULT_INTEREST_RATE = 10.0;

	@Autowired
	LoanAccountRepository repo;

	@Autowired
	LoanApplicationRepository applicationRepo;

	@Autowired
	EmiScheduleRepository emiRepo;

	@Autowired
	BankAccountRepository accountRepo;

	@Autowired
	LedgerService ledger;

	@Autowired
	NotifierService notifier;

	@Autowired
	AuditTrailService auditTrail;

	@Override
	@Transactional
	public LoanAccountDto disburseLoan(Integer applicationId) throws Exception {
		LoanApplication app = applicationRepo.findById(applicationId)
				.orElseThrow(() -> new Exception("LoanApplication not found with ID: " + applicationId));

		if (app.getStatus() != LoanApplicationStatus.Approved) {
			throw new Exception("Application must be APPROVED to disburse (current status: " + app.getStatus() + ").");
		}
		if (repo.findByApplication_ApplicationId(applicationId).isPresent()) {
			throw new Exception("Loan already disbursed for application id: " + applicationId);
		}

		int tenure = app.getTenure() == null || app.getTenure() <= 0 ? 1 : app.getTenure();
		if (tenure > 360) {
			tenure = 360;
		}
		double disbursedAmount = app.getRequestedAmount();
		double rate = DEFAULT_INTEREST_RATE;
		double emiAmount = computeEmi(disbursedAmount, rate, tenure);

		LoanAccount acc = LoanAccount.builder()
				.application(app)
				.customerId(app.getCustomerId())
				.disbursedAmount(disbursedAmount)
				.interestRate(rate)
				.emiAmount(emiAmount)
				.startDate(LocalDate.now())
				.endDate(LocalDate.now().plusMonths(tenure))
				.outstandingBalance(disbursedAmount)
				.status(LoanAccountStatus.Active)
				.build();
		LoanAccount res = repo.save(acc);

		// generate the EMI schedule, one row per month
		double monthlyRate = rate / 12 / 100;
		double outstanding = disbursedAmount;
		for (int i = 1; i <= tenure; i++) {
			double interest = round2(outstanding * monthlyRate);
			double principalPart = round2(emiAmount - interest);
			outstanding = round2(outstanding - principalPart);
			emiRepo.save(EmiSchedule.builder()
					.loanAccount(res)
					.dueDate(res.getStartDate().plusMonths(i))
					.emiAmount(emiAmount)
					.principal(principalPart)
					.interest(interest)
					.status(EmiStatus.Pending)
					.build());
		}

		// cross-module: credit the disbursed amount into the customer's first bank account (+ statement)
		List<BankAccount> accounts = accountRepo.findByCustomerId(res.getCustomerId());
		if (accounts != null && !accounts.isEmpty()) {
			ledger.credit(accounts.get(0).getAccountId(), disbursedAmount);
			notifier.notify(res.getCustomerId(),
					"Loan disbursed: " + disbursedAmount + " credited to account " + accounts.get(0).getAccountId() + ".",
					NotificationCategory.Loan);
		} else {
			notifier.notify(res.getCustomerId(), "Loan disbursed: " + disbursedAmount + " (no linked account to credit).",
					NotificationCategory.Loan);
		}

		// mark the application disbursed
		app.setStatus(LoanApplicationStatus.Disbursed);
		applicationRepo.save(app);

		auditTrail.record(AuditAction.CREATE, AuditModule.LOAN_MODULE);
		log.info("Loan disbursed: account {} for application {} ({} EMIs)", res.getLoanAccountId(), applicationId, tenure);
		return toDto(res);
	}

	@Override
	public LoanAccountDto getById(Integer id) throws Exception {
		LoanAccount e = repo.findById(id).orElseThrow(() -> new Exception("LoanAccount not found with ID: " + id));
		return toDto(e);
	}

	@Override
	public List<LoanAccountDto> getByCustomer(Integer customerId) {
		List<LoanAccountDto> dtos = new ArrayList<>();
		for (LoanAccount e : repo.findByCustomerId(customerId)) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	@Override
	public LoanAccountDto markNPA(Integer loanAccountId) throws Exception {
		LoanAccount e = repo.findById(loanAccountId).orElseThrow(() -> new Exception("LoanAccount not found with ID: " + loanAccountId));
		e.setStatus(LoanAccountStatus.NPA);
		LoanAccount res = repo.save(e);
		log.info("Loan account {} marked NPA", loanAccountId);
		return toDto(res);
	}

	@Override
	public LoanAccountDto closeLoan(Integer loanAccountId) throws Exception {
		LoanAccount e = repo.findById(loanAccountId).orElseThrow(() -> new Exception("LoanAccount not found with ID: " + loanAccountId));
		e.setStatus(LoanAccountStatus.Closed);
		LoanAccount res = repo.save(e);
		log.info("Loan account {} closed", loanAccountId);
		return toDto(res);
	}

	private double computeEmi(double principal, double annualRatePct, int months) {
		double r = annualRatePct / 12 / 100;
		double emi;
		if (r == 0) {
			emi = principal / months;
		} else {
			double f = Math.pow(1 + r, months);
			emi = principal * r * f / (f - 1);
		}
		return round2(emi);
	}

	private double round2(double x) {
		return Math.round(x * 100.0) / 100.0;
	}

	private LoanAccountDto toDto(LoanAccount e) {
		LoanAccountDto dto = new LoanAccountDto();
		dto.setLoanAccountId(e.getLoanAccountId());
		dto.setApplicationId(e.getApplication() == null ? null : e.getApplication().getApplicationId());
		dto.setCustomerId(e.getCustomerId());
		dto.setDisbursedAmount(e.getDisbursedAmount());
		dto.setInterestRate(e.getInterestRate());
		dto.setEmiAmount(e.getEmiAmount());
		dto.setStartDate(e.getStartDate());
		dto.setEndDate(e.getEndDate());
		dto.setOutstandingBalance(e.getOutstandingBalance());
		dto.setStatus(e.getStatus());
		return dto;
	}
}
