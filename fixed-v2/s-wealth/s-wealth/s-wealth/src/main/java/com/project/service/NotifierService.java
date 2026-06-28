package com.project.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entities.Notification;
import com.project.enums.NotificationCategory;
import com.project.enums.NotificationStatus;
import com.project.repository.NotificationRepository;

/**
 * Emits in-app notifications for cross-module events (transfers, loan approvals,
 * deposits, etc.). Best-effort: a failure here must never break the primary
 * operation. The recipient is identified by customerId (stored in userId).
 */
@Service
public class NotifierService {

	@Autowired
	private NotificationRepository repo;

	public void notify(Integer customerId, String message, NotificationCategory category) {
		try {
			repo.save(new Notification(null, customerId, message, category, NotificationStatus.Unread, LocalDateTime.now()));
		} catch (Exception ignored) {
			// notifications are best-effort
		}
	}
}
