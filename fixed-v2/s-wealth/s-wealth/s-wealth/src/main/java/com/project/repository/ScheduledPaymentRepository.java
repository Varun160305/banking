package com.project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.ScheduledPayment;
import com.project.enums.ScheduledPaymentStatus;

@Repository
public interface ScheduledPaymentRepository extends JpaRepository<ScheduledPayment, Integer> {

	List<ScheduledPayment> findByCustomerId(Integer customerId);

	List<ScheduledPayment> findByStatus(ScheduledPaymentStatus status);

	List<ScheduledPayment> findByNextRunDateBefore(LocalDate date);
}
