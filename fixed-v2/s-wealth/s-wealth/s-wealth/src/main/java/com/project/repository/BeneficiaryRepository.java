package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.Beneficiary;
import com.project.enums.BeneficiaryStatus;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Integer> {

	boolean existsByCustomerIdAndAccountNumber(Integer customerId, String accountNumber);

	List<Beneficiary> findByCustomerId(Integer customerId);

	List<Beneficiary> findByCustomerIdAndStatus(Integer customerId, BeneficiaryStatus status);
}
