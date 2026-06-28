package com.project.service;

import java.util.List;
import java.util.Map;

import com.project.dto.EmiScheduleDto;

public interface EmiScheduleService {

	EmiScheduleDto payEMI(Integer emiId) throws Exception;

	List<EmiScheduleDto> getOverdueEMIs();

	List<EmiScheduleDto> getScheduleByLoanAccount(Integer loanAccountId);

	Map<String, Object> getSummary(Integer loanAccountId) throws Exception;
}
