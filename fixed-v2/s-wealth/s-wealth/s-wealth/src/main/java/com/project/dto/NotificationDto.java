package com.project.dto;

import java.time.LocalDateTime;

import com.project.enums.NotificationCategory;
import com.project.enums.NotificationStatus;

public class NotificationDto {

	private Integer notificationId;
	private Integer userId;
	private String message;
	private NotificationCategory category;
	private NotificationStatus status;
	private LocalDateTime createdDate;

	public NotificationDto() {}

	public NotificationDto(Integer notificationId, Integer userId, String message, NotificationCategory category, NotificationStatus status, LocalDateTime createdDate) {
		this.notificationId = notificationId;
		this.userId = userId;
		this.message = message;
		this.category = category;
		this.status = status;
		this.createdDate = createdDate;
	}

	public Integer getNotificationId() { return notificationId; }
	public void setNotificationId(Integer notificationId) { this.notificationId = notificationId; }

	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }

	public NotificationCategory getCategory() { return category; }
	public void setCategory(NotificationCategory category) { this.category = category; }

	public NotificationStatus getStatus() { return status; }
	public void setStatus(NotificationStatus status) { this.status = status; }

	public LocalDateTime getCreatedDate() { return createdDate; }
	public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
