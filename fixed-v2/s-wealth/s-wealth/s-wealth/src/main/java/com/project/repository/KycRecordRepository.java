package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.KycRecord;
import com.project.enums.KycStatus;

@Repository
public interface KycRecordRepository extends JpaRepository<KycRecord, Integer> {

	List<KycRecord> findByCustomerId(Integer customerId);

	List<KycRecord> findByStatus(KycStatus status);

	List<KycRecord> findByDocumentNumber(String documentNumber);

	/** Used by Fund Transfers to gate on a Verified sender KYC. */
	boolean existsByCustomerIdAndStatus(Integer customerId, KycStatus status);
}
