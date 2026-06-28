package com.project.dto;

import java.time.LocalDateTime;

public class AuditLogDto {

	private Integer auditId;
	private Integer userId;
	private String action;
	private String module;
	private String ipAddress;
	private Integer entityId;
	private LocalDateTime timestamp;

	public AuditLogDto() {}

	public Integer getAuditId() { return auditId; }
	public void setAuditId(Integer auditId) { this.auditId = auditId; }

	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }

	public String getAction() { return action; }
	public void setAction(String action) { this.action = action; }

	public String getModule() { return module; }
	public void setModule(String module) { this.module = module; }

	public String getIpAddress() { return ipAddress; }
	public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

	public Integer getEntityId() { return entityId; }
	public void setEntityId(Integer entityId) { this.entityId = entityId; }

	public LocalDateTime getTimestamp() { return timestamp; }
	public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
