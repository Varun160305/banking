package com.project.seed;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.project.entities.AmlFlag;
import com.project.entities.AuditLog;
import com.project.entities.BankAccount;
import com.project.entities.BankingReport;
import com.project.entities.Beneficiary;
import com.project.entities.AccountStatement;
import com.project.entities.EmiSchedule;
import com.project.entities.FixedDeposit;
import com.project.entities.FundTransfer;
import com.project.entities.InvestmentHolding;
import com.project.entities.KycRecord;
import com.project.entities.LoanAccount;
import com.project.entities.LoanApplication;
import com.project.entities.Notification;
import com.project.entities.Portfolio;
import com.project.entities.ScheduledPayment;
import com.project.entities.User;
import com.project.enums.AccountStatus;
import com.project.enums.AccountType;
import com.project.enums.AmlStatus;
import com.project.enums.AssetType;
import com.project.enums.AuditAction;
import com.project.enums.AuditModule;
import com.project.enums.BeneficiaryStatus;
import com.project.enums.Currency;
import com.project.enums.DocumentType;
import com.project.enums.EmiStatus;
import com.project.enums.FixedDepositStatus;
import com.project.enums.FlagType;
import com.project.enums.Frequency;
import com.project.enums.HoldingStatus;
import com.project.enums.KycStatus;
import com.project.enums.LoanAccountStatus;
import com.project.enums.LoanApplicationStatus;
import com.project.enums.LoanType;
import com.project.enums.NotificationCategory;
import com.project.enums.NotificationStatus;
import com.project.enums.ReportScope;
import com.project.enums.Role;
import com.project.enums.ScheduledPaymentStatus;
import com.project.enums.Severity;
import com.project.enums.TransferStatus;
import com.project.enums.TransferType;
import com.project.enums.UserStatus;
import com.project.repository.AmlFlagRepository;
import com.project.repository.AuditLogRepository;
import com.project.repository.AccountStatementRepository;
import com.project.repository.BankAccountRepository;
import com.project.repository.BankingReportRepository;
import com.project.repository.BeneficiaryRepository;
import com.project.repository.EmiScheduleRepository;
import com.project.repository.FixedDepositRepository;
import com.project.repository.FundTransferRepository;
import com.project.repository.InvestmentHoldingRepository;
import com.project.repository.KycRecordRepository;
import com.project.repository.LoanAccountRepository;
import com.project.repository.LoanApplicationRepository;
import com.project.repository.NotificationRepository;
import com.project.repository.PortfolioRepository;
import com.project.repository.ScheduledPaymentRepository;
import com.project.repository.UserRepository;

/**
 * Seeds one representative row per module (and one login-ready user per role)
 * the first time the application starts against an empty database.
 */
@Component
public class DataSeeder implements CommandLineRunner {

	private final UserRepository userRepo;
	private final AuditLogRepository auditRepo;
	private final BankAccountRepository accountRepo;
	private final AccountStatementRepository statementRepo;
	private final FundTransferRepository transferRepo;
	private final BeneficiaryRepository beneficiaryRepo;
	private final ScheduledPaymentRepository scheduledRepo;
	private final LoanApplicationRepository loanAppRepo;
	private final LoanAccountRepository loanAccRepo;
	private final EmiScheduleRepository emiRepo;
	private final FixedDepositRepository fdRepo;
	private final PortfolioRepository portfolioRepo;
	private final InvestmentHoldingRepository holdingRepo;
	private final KycRecordRepository kycRepo;
	private final AmlFlagRepository amlRepo;
	private final BankingReportRepository reportRepo;
	private final NotificationRepository notificationRepo;

	public DataSeeder(UserRepository userRepo, AuditLogRepository auditRepo, BankAccountRepository accountRepo,
			AccountStatementRepository statementRepo, FundTransferRepository transferRepo,
			BeneficiaryRepository beneficiaryRepo, ScheduledPaymentRepository scheduledRepo,
			LoanApplicationRepository loanAppRepo, LoanAccountRepository loanAccRepo, EmiScheduleRepository emiRepo,
			FixedDepositRepository fdRepo, PortfolioRepository portfolioRepo, InvestmentHoldingRepository holdingRepo,
			KycRecordRepository kycRepo, AmlFlagRepository amlRepo, BankingReportRepository reportRepo,
			NotificationRepository notificationRepo) {
		this.userRepo = userRepo;
		this.auditRepo = auditRepo;
		this.accountRepo = accountRepo;
		this.statementRepo = statementRepo;
		this.transferRepo = transferRepo;
		this.beneficiaryRepo = beneficiaryRepo;
		this.scheduledRepo = scheduledRepo;
		this.loanAppRepo = loanAppRepo;
		this.loanAccRepo = loanAccRepo;
		this.emiRepo = emiRepo;
		this.fdRepo = fdRepo;
		this.portfolioRepo = portfolioRepo;
		this.holdingRepo = holdingRepo;
		this.kycRepo = kycRepo;
		this.amlRepo = amlRepo;
		this.reportRepo = reportRepo;
		this.notificationRepo = notificationRepo;
	}

	@Override
	public void run(String... args) {
		if (userRepo.count() > 0) {
			return; // already seeded
		}

		// ----- 4.1 Identity & Access Management -----
		// passwords are BCrypt-hashed; log in with the plaintext shown in the comments
		BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
		User admin = userRepo.save(new User(null, "Asha Admin", Role.ADMIN, "admin@bank.com", "9000000001", 1, UserStatus.Active, enc.encode("admin123")));
		User holder = userRepo.save(new User(null, "Hari Holder", Role.ACCOUNTHOLDER, "holder@bank.com", "9000000002", 1, UserStatus.Active, enc.encode("holder123")));
		userRepo.save(new User(null, "Rita RM", Role.RELATIONSHIPMANAGER, "rm@bank.com", "9000000003", 2, UserStatus.Active, enc.encode("rm123")));
		User loanOfficer = userRepo.save(new User(null, "Leo Loan", Role.LOANOFFICER, "loan@bank.com", "9000000004", 2, UserStatus.Active, enc.encode("loan123")));
		userRepo.save(new User(null, "Omar Ops", Role.OPERATIONS, "ops@bank.com", "9000000005", 3, UserStatus.Active, enc.encode("ops123")));
		userRepo.save(new User(null, "Cara Compliance", Role.COMPLIANCE, "compliance@bank.com", "9000000006", 3, UserStatus.Active, enc.encode("comp123")));

		// 3 audit logs (action / module stored as plain strings)
		auditRepo.save(AuditLog.builder().userId(admin.getUserId()).action("CREATE").module("USER_MODULE").ipAddress("127.0.0.1").build());
		auditRepo.save(AuditLog.builder().userId(holder.getUserId()).action("LOGIN").module("USER_MODULE").ipAddress("127.0.0.1").build());
		auditRepo.save(AuditLog.builder().userId(loanOfficer.getUserId()).action("CREATE").module("LOAN_MODULE").ipAddress("127.0.0.1").build());

		// ----- 4.2 Customer Account Management -----
		// 3 bank accounts
		BankAccount bankAcc1 = accountRepo.save(BankAccount.builder().customerId(101).accountType(AccountType.Savings)
				.accountNumber("SB1000100001").balance(75000.0).interestRate(3.5).openDate(LocalDate.of(2024, 1, 15))
				.status(AccountStatus.Active).build());
		accountRepo.save(BankAccount.builder().customerId(102).accountType(AccountType.Current)
				.accountNumber("CA1000100002").balance(250000.0).interestRate(0.0).openDate(LocalDate.of(2023, 6, 10))
				.status(AccountStatus.Active).build());
		accountRepo.save(BankAccount.builder().customerId(103).accountType(AccountType.SalaryAccount)
				.accountNumber("SA1000100003").balance(40000.0).interestRate(3.0).openDate(LocalDate.of(2025, 2, 1))
				.status(AccountStatus.Active).build());

		// 2 statements (linked to account 1)
		statementRepo.save(AccountStatement.builder().bankAccount(bankAcc1).periodStart(LocalDate.of(2025, 5, 1))
				.periodEnd(LocalDate.of(2025, 5, 31)).openingBalance(60000.0).closingBalance(75000.0)
				.transactionCount(12).generatedDate(LocalDate.of(2025, 6, 1)).build());
		statementRepo.save(AccountStatement.builder().bankAccount(bankAcc1).periodStart(LocalDate.of(2025, 6, 1))
				.periodEnd(LocalDate.of(2025, 6, 30)).openingBalance(75000.0).closingBalance(81000.0)
				.transactionCount(9).generatedDate(LocalDate.of(2025, 7, 1)).build());

		// ----- 4.3 Fund Transfers & Payments -----
		// 4 transfers (mix of COMPLETED / FAILED)
	
		BankAccount bankAcc2 = accountRepo.findById(2).orElseThrow();

		transferRepo.save(FundTransfer.builder()
		        .fromAccount(bankAcc1)
		        .toAccount(bankAcc2)
		        .amount(5000.0)
		        .currency(Currency.INR)
		        .transferType(TransferType.Internal)
		        .remarks("Rent payment")
		        .transferDate(LocalDate.of(2025, 6, 12))
		        .status(TransferStatus.Completed)
		        .build());

		transferRepo.save(FundTransfer.builder()
		        .fromAccount(bankAcc2)
		        .toAccount(bankAcc1)
		        .amount(12000.0)
		        .currency(Currency.INR)
		        .transferType(TransferType.IntraBank)
		        .remarks("Invoice settlement")
		        .transferDate(LocalDate.of(2025, 6, 13))
		        .status(TransferStatus.Completed)
		        .build());

		transferRepo.save(FundTransfer.builder()
		        .fromAccount(bankAcc1)
		        .toAccount(bankAcc2)
		        .amount(800.0)
		        .currency(Currency.INR)
		        .transferType(TransferType.Internal)
		        .remarks("Insufficient funds")
		        .transferDate(LocalDate.of(2025, 6, 14))
		        .status(TransferStatus.Failed)
		        .build());

		transferRepo.save(FundTransfer.builder()
		        .fromAccount(bankAcc2)
		        .toAccount(bankAcc1)
		        .amount(2500.0)
		        .currency(Currency.INR)
		        .transferType(TransferType.ScheduledStanding)
		        .remarks("Account dormant")
		        .transferDate(LocalDate.of(2025, 6, 15))
		        .status(TransferStatus.Failed)
		        .build());


		// 3 beneficiaries
		Beneficiary ben1 = beneficiaryRepo.save(Beneficiary.builder().customerId(101).accountNumber("SB2000200002")
				.name("Meena Sharma").bankName("WealthWise Bank").status(BeneficiaryStatus.Active).build());
		Beneficiary ben2 = beneficiaryRepo.save(Beneficiary.builder().customerId(101).accountNumber("HD3000300003")
				.name("Ravi Kumar").bankName("HDFC Bank").status(BeneficiaryStatus.Active).build());
		beneficiaryRepo.save(Beneficiary.builder().customerId(102).accountNumber("IC4000400004")
				.name("Sneha Rao").bankName("ICICI Bank").status(BeneficiaryStatus.Active).build());

		// 2 scheduled payments
		scheduledRepo.save(ScheduledPayment.builder().customerId(101).fromAccountId(1).beneficiary(ben1).amount(1500.0)
				.frequency(Frequency.Monthly).nextRunDate(LocalDate.of(2025, 7, 1)).status(ScheduledPaymentStatus.Active).build());
		scheduledRepo.save(ScheduledPayment.builder().customerId(101).fromAccountId(1).beneficiary(ben2).amount(3000.0)
				.frequency(Frequency.Weekly).nextRunDate(LocalDate.of(2025, 7, 5)).status(ScheduledPaymentStatus.Active).build());

		// ----- 4.4 Loan & Credit Management -----
		// 2 applications (APPROVED / REJECTED)
		LoanApplication loanApp1 = loanAppRepo.save(LoanApplication.builder().customerId(101).loanType(LoanType.Home)
				.requestedAmount(2500000.0).tenure(240).purpose("House purchase").applicationDate(LocalDate.of(2025, 3, 20))
				.status(LoanApplicationStatus.Approved).build());
		loanAppRepo.save(LoanApplication.builder().customerId(102).loanType(LoanType.Personal)
				.requestedAmount(500000.0).tenure(36).purpose("Debt consolidation").applicationDate(LocalDate.of(2025, 3, 25))
				.status(LoanApplicationStatus.Rejected).build());

		// 1 loan account (for the approved application)
		LoanAccount loanAcc1 = loanAccRepo.save(LoanAccount.builder().application(loanApp1).customerId(101)
				.disbursedAmount(2500000.0).interestRate(8.5).emiAmount(21700.0).startDate(LocalDate.of(2025, 4, 1))
				.endDate(LocalDate.of(2045, 4, 1)).outstandingBalance(2480000.0).status(LoanAccountStatus.Active).build());

		// 3 EMI schedules
		emiRepo.save(EmiSchedule.builder().loanAccount(loanAcc1).dueDate(LocalDate.of(2025, 7, 1)).emiAmount(21700.0)
				.principal(4200.0).interest(17500.0).status(EmiStatus.Pending).build());
		emiRepo.save(EmiSchedule.builder().loanAccount(loanAcc1).dueDate(LocalDate.of(2025, 8, 1)).emiAmount(21700.0)
				.principal(4230.0).interest(17470.0).status(EmiStatus.Pending).build());
		emiRepo.save(EmiSchedule.builder().loanAccount(loanAcc1).dueDate(LocalDate.of(2025, 9, 1)).emiAmount(21700.0)
				.principal(4260.0).interest(17440.0).status(EmiStatus.Pending).build());

		// ----- 4.5 Investment & Portfolio Management -----
		// 2 fixed deposits
		fdRepo.save(FixedDeposit.builder().customerId(101).accountId(1).principal(100000.0).interestRate(7.0).tenure(12)
				.maturityDate(LocalDate.of(2026, 1, 15)).maturityAmount(107000.0).status(FixedDepositStatus.Active).build());
		fdRepo.save(FixedDeposit.builder().customerId(102).accountId(2).principal(50000.0).interestRate(6.5).tenure(24)
				.maturityDate(LocalDate.of(2027, 6, 1)).maturityAmount(56500.0).status(FixedDepositStatus.Active).build());

		// 2 portfolios with 3 holdings each
		Portfolio portfolio1 = portfolioRepo.save(Portfolio.builder().customerId(101).relationshipManagerId(3)
				.totalValue(348000.0).assetAllocation("{\"Equity\":60,\"Debt\":40}").createdDate(LocalDate.of(2024, 2, 1))
				.lastReviewedDate(LocalDate.of(2025, 6, 1)).build());
		holdingRepo.save(InvestmentHolding.builder().portfolio(portfolio1).assetType(AssetType.MutualFund)
				.assetName("BlueChip Growth Fund").units(500.0).purchaseValue(200000.0).currentValue(235000.0).status(HoldingStatus.Active).build());
		holdingRepo.save(InvestmentHolding.builder().portfolio(portfolio1).assetType(AssetType.Equity)
				.assetName("ACME Ltd").units(100.0).purchaseValue(50000.0).currentValue(62000.0).status(HoldingStatus.Active).build());
		holdingRepo.save(InvestmentHolding.builder().portfolio(portfolio1).assetType(AssetType.Bond)
				.assetName("Govt Bond 2030").units(50.0).purchaseValue(50000.0).currentValue(51000.0).status(HoldingStatus.Active).build());

		Portfolio portfolio2 = portfolioRepo.save(Portfolio.builder().customerId(102).relationshipManagerId(3)
				.totalValue(241000.0).assetAllocation("{\"Equity\":40,\"Debt\":60}").createdDate(LocalDate.of(2024, 5, 1))
				.lastReviewedDate(LocalDate.of(2025, 6, 1)).build());
		holdingRepo.save(InvestmentHolding.builder().portfolio(portfolio2).assetType(AssetType.FD)
				.assetName("WealthWise FD").units(1.0).purchaseValue(100000.0).currentValue(107000.0).status(HoldingStatus.Active).build());
		holdingRepo.save(InvestmentHolding.builder().portfolio(portfolio2).assetType(AssetType.MutualFund)
				.assetName("Index Fund 50").units(300.0).purchaseValue(90000.0).currentValue(96000.0).status(HoldingStatus.Active).build());
		holdingRepo.save(InvestmentHolding.builder().portfolio(portfolio2).assetType(AssetType.Equity)
				.assetName("Tech Corp").units(80.0).purchaseValue(40000.0).currentValue(38000.0).status(HoldingStatus.Active).build());

		// ----- 4.6 KYC & Compliance Management -----
		// 2 KYC records (verifiedBy = the seeded Compliance user id = 6)
		kycRepo.save(KycRecord.builder().customerId(101).documentType(DocumentType.Passport).documentNumber("P1234567")
				.verifiedBy(6).verificationDate(LocalDate.of(2025, 1, 10)).status(KycStatus.Verified).build());
		kycRepo.save(KycRecord.builder().customerId(102).documentType(DocumentType.NationalID).documentNumber("N9988776")
				.status(KycStatus.Pending).build());

		// 2 AML flags (linked to account 1)
		amlRepo.save(AmlFlag.builder().bankAccount(bankAcc1).transactionId(1).flagType(FlagType.LargeTransaction)
				.severity(Severity.High).raisedDate(LocalDate.of(2025, 6, 12)).status(AmlStatus.Open).build());
		amlRepo.save(AmlFlag.builder().bankAccount(bankAcc1).transactionId(2).flagType(FlagType.UnusualPattern)
				.severity(Severity.Medium).raisedDate(LocalDate.of(2025, 6, 14)).status(AmlStatus.Investigated).build());

		// ----- 4.7 Banking Analytics & Reporting -----
		// 2 banking reports (scope = enum per the PDF; metrics = JSON; generatedDate auto via @CreationTimestamp)
		reportRepo.save(BankingReport.builder().scope(ReportScope.Branch)
				.metrics("{\"totalDeposits\":325000,\"loanBookValue\":2480000,\"npaRatio\":0.0,\"emiCollectionRate\":98.5,\"newAccountsOpened\":3}").build());
		reportRepo.save(BankingReport.builder().scope(ReportScope.Product)
				.metrics("{\"totalDeposits\":150000,\"loanBookValue\":0,\"npaRatio\":0.0,\"emiCollectionRate\":0.0,\"newAccountsOpened\":1}").build());

		// ----- 4.8 Notifications & Alerts -----
		// 5 notifications (mix of categories and read/unread)
		notificationRepo.save(Notification.builder().userId(2).message("Your account was debited INR 5,000 for Rent payment.").category(NotificationCategory.Transaction).status(NotificationStatus.Unread).build());
		notificationRepo.save(Notification.builder().userId(2).message("Your loan application has been approved.").category(NotificationCategory.Loan).status(NotificationStatus.Unread).build());
		notificationRepo.save(Notification.builder().userId(2).message("Your fixed deposit has matured; funds credited.").category(NotificationCategory.Investment).status(NotificationStatus.Read).build());
		notificationRepo.save(Notification.builder().userId(2).message("Your KYC document expires next month.").category(NotificationCategory.KYC).status(NotificationStatus.Unread).build());
		notificationRepo.save(Notification.builder().userId(6).message("An AML flag has been raised for review.").category(NotificationCategory.Compliance).status(NotificationStatus.Unread).build());

		System.out.println(">>> WealthWise sample data seeded successfully.");
	}
}
