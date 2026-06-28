package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.AmlFlag;
import com.project.enums.AmlStatus;
import com.project.enums.Severity;

@Repository
public interface AmlFlagRepository extends JpaRepository<AmlFlag, Integer> {

	List<AmlFlag> findByBankAccount_AccountId(Integer accountId);

	List<AmlFlag> findByStatus(AmlStatus status);

	List<AmlFlag> findBySeverity(Severity severity);
}
