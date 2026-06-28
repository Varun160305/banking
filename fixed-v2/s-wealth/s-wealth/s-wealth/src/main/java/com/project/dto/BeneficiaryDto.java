package com.project.dto;

import com.project.enums.BeneficiaryStatus;

public class BeneficiaryDto {

	private Integer beneficiaryId;
	private Integer customerId;
	private String accountNumber;
	private String name;
	private String bankName;
	private BeneficiaryStatus status;

	public BeneficiaryDto() {}

	public BeneficiaryDto(Integer beneficiaryId, Integer customerId, String accountNumber, String name, String bankName, BeneficiaryStatus status) {
		this.beneficiaryId = beneficiaryId;
		this.customerId = customerId;
		this.accountNumber = accountNumber;
		this.name = name;
		this.bankName = bankName;
		this.status = status;
	}

	public Integer getBeneficiaryId() { return beneficiaryId; }
	public void setBeneficiaryId(Integer beneficiaryId) { this.beneficiaryId = beneficiaryId; }

	public Integer getCustomerId() { return customerId; }
	public void setCustomerId(Integer customerId) { this.customerId = customerId; }

	public String getAccountNumber() { return accountNumber; }
	public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getBankName() { return bankName; }
	public void setBankName(String bankName) { this.bankName = bankName; }

	public BeneficiaryStatus getStatus() { return status; }
	public void setStatus(BeneficiaryStatus status) { this.status = status; }
}
