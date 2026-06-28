package com.project.service;

import java.time.LocalDate;
import java.util.List;

import com.project.dto.ScheduledPaymentDto;

public interface ScheduledPaymentService {

	ScheduledPaymentDto createSchedule(ScheduledPaymentDto dto) throws Exception;

	List<ScheduledPaymentDto> getSchedulesByCustomer(Integer customerId);

	ScheduledPaymentDto pauseSchedule(Integer id) throws Exception;

	ScheduledPaymentDto cancelSchedule(Integer id) throws Exception;

	List<ScheduledPaymentDto> getDuePayments(LocalDate date);
}
