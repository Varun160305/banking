# WealthWise — Banking & Wealth Management Platform

Spring Boot REST backend covering all 8 modules from the spec, with JWT-secured,
role-based access. Built with the same layered pattern throughout:

```
controller  ->  service (interface)  ->  serviceimplementation  ->  repository  ->  entities
                         (DTOs carry data across the layers)
```

## Tech stack
- Java 21, Spring Boot 3.3.4 (Web, Data JPA, Security, Validation)
- MySQL (auto-creates the `wealthwise_banking` schema on first run)
- JWT auth (jjwt 0.9.1) — `JwtUtil` / `JwtFilter` / `SecurityConfig`
- Lombok for getters/setters/constructors

## Modules & entities
| # | Module | Entities | Base path(s) |
|---|--------|----------|--------------|
| 4.1 | Identity & Access Management | User, AuditLog | `/api/auth`, `/api/users`, `/api/audit-logs` |
| 4.2 | Customer Account Management | BankAccount, AccountStatement | `/api/bank-accounts`, `/api/account-statements` |
| 4.3 | Fund Transfers & Payments | FundTransfer, Beneficiary, ScheduledPayment | `/api/fund-transfers`, `/api/beneficiaries`, `/api/scheduled-payments` |
| 4.4 | Loan & Credit Management | LoanApplication, LoanAccount, EmiSchedule | `/api/loan-applications`, `/api/loan-accounts`, `/api/emi-schedules` |
| 4.5 | Investment & Portfolio Management | FixedDeposit, Portfolio, InvestmentHolding | `/api/fixed-deposits`, `/api/portfolios` (holdings nested) |
| 4.6 | KYC & Compliance Management | KycRecord, AmlFlag | `/api/kyc-records`, `/api/aml-flags` |
| 4.7 | Banking Analytics & Reporting | BankingReport | `/api/banking-reports` |
| 4.8 | Notifications & Alerts | Notification | `/api/notifications` |

Most module controllers expose the same CRUD endpoints:
`POST /create`, `GET /all`, `GET /get/{id}`, `PUT /update/{id}`, `DELETE /delete/{id}`
(the User module keeps your original method names: `/alluser`, etc.).

> **Fund Transfers & Payments (4.3) is different** — it follows the FTP sprint
> stories, not the generic CRUD shape. Endpoints (see `FTP_Collection.json`):
> `POST /api/fund-transfers`, `GET /api/fund-transfers?fromAccountId=` / `?toAccountId=`,
> `GET /api/fund-transfers/{id}`, `PATCH /api/fund-transfers/{id}/reverse`,
> `GET /api/fund-transfers/account/{id}/statement?from=&to=`;
> `POST /api/beneficiaries`, `GET /api/beneficiaries?customerId=`, `GET /api/beneficiaries/{id}`,
> `DELETE /api/beneficiaries/{id}` (soft delete);
> `POST /api/scheduled-payments`, `GET /api/scheduled-payments?customerId=`,
> `PATCH /api/scheduled-payments/{id}/pause`, `PATCH /api/scheduled-payments/{id}/cancel`.

> **Loan & Credit Management (4.4) is also story-shaped**, not generic CRUD
> (see `LCM_Collection.json`). Lifecycle: submit → under-review → approve → disburse → pay EMIs.
> `POST /api/loan-applications`, `GET /api/loan-applications?customerId=` / `?status=`,
> `GET /api/loan-applications/{id}`, `PATCH /api/loan-applications/{id}/under-review` / `/approve` / `/reject`;
> `POST /api/loan-accounts/disburse?applicationId=` (400 if not APPROVED or already disbursed),
> `GET /api/loan-accounts?customerId=`, `GET /api/loan-accounts/{id}`,
> `PATCH /api/loan-accounts/{id}/mark-npa` / `/close`;
> `GET /api/emi-schedules?loanAccountId=`, `GET /api/emi-schedules/overdue`,
> `PATCH /api/emi-schedules/{id}/pay` (400 if already PAID),
> `GET /api/emi-schedules/loan-account/{id}/summary`.

> **IAM (4.1) & Notifications (4.8) are story-shaped** (see `IAM_NAL_Collection.json`).
> Passwords are **BCrypt-hashed**; login returns `{token, role, userId}`.
> `POST /api/auth/login`; `POST/GET /api/users`, `GET /api/users/{id}`,
> `PATCH /api/users/{id}/status?status=` / `/lock` / `/unlock` (ADMIN);
> `GET /api/audit-logs` (pageable, `?userId=` / `?module=`), `POST /api/audit-logs` (internal/public);
> `POST /api/notifications`, `GET /api/notifications/user/{userId}` / `/unread` / `/count`,
> `PATCH /api/notifications/{id}/read`, `PATCH /api/notifications/user/{userId}/read-all`.

> **Customer Accounts (4.2) & KYC/Compliance (4.6) are story-shaped** (see `CAM_KYC_Collection.json`).
> `POST /api/bank-accounts`, `GET /api/bank-accounts?customerId=`, `GET /api/bank-accounts/{id}`,
> `GET /api/bank-accounts/number/{accountNumber}`, `PATCH /api/bank-accounts/{id}/close`;
> `POST /api/account-statements?accountId=&from=&to=`, `GET /api/account-statements?accountId=`;
> `POST /api/kyc-records`, `GET /api/kyc-records?customerId=`, `GET /api/kyc-records/{id}`,
> `PATCH /api/kyc-records/{id}/verify?verifiedBy=` / `/reject`;
> `POST /api/aml-flags`, `GET /api/aml-flags?accountId=` / `?status=`,
> `PATCH /api/aml-flags/{id}/investigate` / `/clear`.

> **Investment & Portfolio (4.5) & Banking Analytics (4.7) are story-shaped** (see `IPM_BAR_Collection.json`).
> Holdings are **nested under portfolios** (no standalone `/holdings`). `BankingReport.scope` is free-text `String`
> with JSON `metrics`.
> `POST /api/fixed-deposits`, `GET /api/fixed-deposits?customerId=` / `{id}`, `PATCH /api/fixed-deposits/{id}/mature` / `/close-premature`, `GET /api/fixed-deposits/maturing?before=`;
> `POST /api/portfolios`, `GET /api/portfolios?customerId=` / `{id}`, `POST /api/portfolios/{id}/holdings`,
> `GET /api/portfolios/{id}/holdings`, `PATCH /api/portfolios/{id}/holdings/{holdingId}/redeem`;
> `POST /api/banking-reports?scope=`, `GET /api/banking-reports`, `GET /api/banking-reports/{id}`, `GET /api/banking-reports/summary`.

## Roles (RBAC)
`ACCOUNTHOLDER`, `RELATIONSHIPMANAGER`, `LOANOFFICER`, `OPERATIONS`, `COMPLIANCE`, `ADMIN`
— enforced per module in `SecurityConfig`. CORS is open to `http://localhost:3000`.

## Run it (quick)
1. Start MySQL locally. Update credentials in `src/main/resources/application.properties`
   if yours differ (defaults: `root` / `root`).
2. From the project root:
   ```
   mvn spring-boot:run
   ```
   The app starts on **http://localhost:8098** (set by `server.port` in
   `application.properties`). In Spring Tool Suite: *Run As → Spring Boot App* on
   `WealthWiseApplication`.

> Maven on the corporate network: the SSL-bypass flags live in `.mvn/maven.config`,
> so a plain `mvn` command resolves dependencies from Maven Central.

---

## Setup on a fresh PC (Windows, no Maven installed)

These are the exact steps to get the project building and running on a clean
machine. Commands are PowerShell.

### 1. Prerequisites
- **JDK 21** — verify: `java -version` (should say 21). Note the install path,
  e.g. `C:\Program Files\Java\jdk-21`.
- **MySQL 8** running locally on port 3306, user `root` / password `root`
  (or edit `src/main/resources/application.properties` to match yours).
  The schema is created automatically (`createDatabaseIfNotExist=true`).

### 2. Install Maven (no admin needed)
Pick one:

**Option A — winget**
```powershell
winget install Apache.Maven
```

**Option B — manual download (what we used)**
```powershell
$tools = "$env:USERPROFILE\tools"; New-Item -ItemType Directory -Force $tools | Out-Null
# check current version at https://dlcdn.apache.org/maven/maven-3/ , then:
Invoke-WebRequest "https://dlcdn.apache.org/maven/maven-3/3.9.16/binaries/apache-maven-3.9.16-bin.zip" -OutFile "$tools\maven.zip"
Expand-Archive "$tools\maven.zip" -DestinationPath $tools -Force
# mvn is now at: $tools\apache-maven-3.9.16\bin\mvn.cmd
```

Maven needs `JAVA_HOME`. Set it for the current shell (point at your JDK 21):
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:Path = "$env:USERPROFILE\tools\apache-maven-3.9.16\bin;$env:Path"
mvn -v          # should print Maven 3.9.x + Java 21
```
(To make `JAVA_HOME` / `Path` permanent, add them via *System → Environment Variables*.)

### 3. Run the unit tests
```powershell
cd <project-root-with-pom.xml>
mvn test
```
Expected: `Tests run: 41, Failures: 0, Errors: 0` → `BUILD SUCCESS`
(7 Fund-Transfers + 7 Loan + 9 IAM/Notifications + 10 Account/KYC/AML + 8 Investment/Analytics tests; Maven downloads test deps on first run).

### 4. Run the app
```powershell
mvn spring-boot:run
```
App comes up on **http://localhost:8098** and seeds sample data on first start.

### 5. If you changed enums / entities and re-run against an existing DB
Hibernate (`ddl-auto=update`) does **not** rewrite existing MySQL `enum` columns,
so old rows can break new code (`Data truncated for column 'status'`). Reset once:
```powershell
& "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -uroot -proot -e "DROP DATABASE IF EXISTS wealthwise_banking;"
```
Then start the app again — it recreates and re-seeds the schema cleanly.

### 6. IDE note (Lombok)
The Fund-Transfers entities use Lombok (`@Data` / `@Builder` / `@Slf4j`).
- **Maven CLI**: works out of the box (Lombok is an annotation processor on the build path).
- **Eclipse / Spring Tool Suite**: install the Lombok agent once so the editor
  resolves generated getters/setters — run `java -jar <path-to-lombok.jar>` (the jar
  is in `~/.m2/repository/org/projectlombok/lombok/...`) and restart STS. Without
  this the IDE shows false red errors, but `mvn` still compiles fine.

### Smoke test (after the app is up)
```powershell
$base = "http://localhost:8098"
$tok = (Invoke-RestMethod "$base/api/auth/login" -Method Post -ContentType "application/json" `
        -Body (@{email="holder@bank.com";password="holder123"} | ConvertTo-Json)).token
$h = @{ Authorization = "Bearer $tok" }
Invoke-RestMethod "$base/api/beneficiaries?customerId=101" -Headers $h          # seeded beneficiaries
Invoke-RestMethod "$base/api/fund-transfers" -Headers $h -Method Post -ContentType "application/json" `
  -Body (@{fromAccountId=1;toAccountId=2;amount=1500;transferType="Internal"} | ConvertTo-Json)   # -> Completed
```
Postman collections live at the project root: `IAM_NAL_Collection.json`, `FTP_Collection.json`, `LCM_Collection.json`.

## Auth flow (example)
1. **Login** to get a JWT (seeded users, e.g. `admin@bank.com` / `admin123`):
   ```
   POST /api/auth/login
   { "email":"admin@bank.com", "password":"admin123" }
   ```
   Response: `{ "token":"<jwt>", "role":"ADMIN", "userId":1 }` (or `401`).
2. **Call any secured endpoint** with the header:
   ```
   Authorization: Bearer <token>
   ```
3. **Create users** (ADMIN only — registration is no longer public). Passwords are BCrypt-hashed:
   ```
   POST /api/users        (Authorization: Bearer <admin-token>)
   { "name":"Asha", "email":"asha@bank.com", "password":"pass123",
     "role":"ACCOUNTHOLDER", "phone":"9999999999", "branchId":1 }
   ```
