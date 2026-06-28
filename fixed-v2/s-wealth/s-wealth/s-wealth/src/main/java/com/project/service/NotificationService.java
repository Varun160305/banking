package com.project.service;

import java.util.List;

import com.project.dto.NotificationDto;

public interface NotificationService {

	NotificationDto sendNotification(NotificationDto dto);

	List<NotificationDto> getByUser(Integer userId);

	List<NotificationDto> getUnread(Integer userId);

	NotificationDto markAsRead(Integer id) throws Exception;

	int markAllAsRead(Integer userId);

	long getUnreadCount(Integer userId);
}
