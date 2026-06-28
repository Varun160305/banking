package com.project.dto;

import java.time.LocalDate;

import com.project.enums.DocumentType;
import com.project.enums.KycStatus;

public class KycRecordDto {

	private Integer kycId;
	private Integer customerId;
	private DocumentType documentType;
	private String documentNumber;
	private Integer verifiedBy;
	private LocalDate verificationDate;
	private KycStatus status;

	public KycRecordDto() {}

	public KycRecordDto(Integer kycId, Integer customerId, DocumentType documentType, String documentNumber, Integer verifiedBy, LocalDate verificationDate, KycStatus status) {
		this.kycId = kycId;
		this.customerId = customerId;
		this.documentType = documentType;
		this.documentNumber = documentNumber;
		this.verifiedBy = verifiedBy;
		this.verificationDate = verificationDate;
		this.status = status;
	}

	public Integer getKycId() { return kycId; }
	public void setKycId(Integer kycId) { this.kycId = kycId; }

	public Integer getCustomerId() { return customerId; }
	public void setCustomerId(Integer customerId) { this.customerId = customerId; }

	public DocumentType getDocumentType() { return documentType; }
	public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }

	public String getDocumentNumber() { return documentNumber; }
	public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

	public Integer getVerifiedBy() { return verifiedBy; }
	public void setVerifiedBy(Integer verifiedBy) { this.verifiedBy = verifiedBy; }

	public LocalDate getVerificationDate() { return verificationDate; }
	public void setVerificationDate(LocalDate verificationDate) { this.verificationDate = verificationDate; }

	public KycStatus getStatus() { return status; }
	public void setStatus(KycStatus status) { this.status = status; }
}
