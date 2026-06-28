package com.project.serviceimplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.dto.AuditLogDto;
import com.project.entities.AuditLog;
import com.project.repository.AuditLogRepository;
import com.project.service.AuditLogService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditLogServiceImplementation implements AuditLogService {

	@Autowired
	AuditLogRepository repo;

	@Override
	public void logAction(Integer userId, String action, String module, String ipAddress, Integer entityId) {
		try {
			AuditLog entry = AuditLog.builder()
					.userId(userId)
					.action(action)
					.module(module)
					.ipAddress(ipAddress)
					.build();
			repo.save(entry);
			log.info("AUDIT user={} action={} module={} entityId={} ip={}", userId, action, module, entityId, ipAddress);
		} catch (Exception ex) {
			// auditing must never break the primary operation
			log.warn("Failed to write audit log: {}", ex.getMessage());
		}
	}

	@Override
	public Page<AuditLogDto> getPage(Pageable pageable) {
		return repo.findAll(pageable).map(this::toDto);
	}

	@Override
	public List<AuditLogDto> findByUserId(Integer userId) {
		return toDtoList(repo.findByUserId(userId));
	}

	@Override
	public List<AuditLogDto> findByModule(String module) {
		return toDtoList(repo.findByModule(module));
	}

	private List<AuditLogDto> toDtoList(List<AuditLog> list) {
		List<AuditLogDto> dtos = new ArrayList<>();
		for (AuditLog e : list) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	private AuditLogDto toDto(AuditLog e) {
		AuditLogDto dto = new AuditLogDto();
		dto.setAuditId(e.getAuditId());
		dto.setUserId(e.getUserId());
		dto.setAction(e.getAction());
		dto.setModule(e.getModule());
		dto.setIpAddress(e.getIpAddress());
		dto.setTimestamp(e.getTimestamp());
		return dto;
	}
}
