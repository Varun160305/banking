package com.project.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.dto.AuditLogDto;

public interface AuditLogService {

	/** Shared audit entry-point called by all modules. */
	void logAction(Integer userId, String action, String module, String ipAddress, Integer entityId);

	Page<AuditLogDto> getPage(Pageable pageable);

	List<AuditLogDto> findByUserId(Integer userId);

	List<AuditLogDto> findByModule(String module);
}
