package com.project.serviceimplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.NotificationDto;
import com.project.entities.Notification;
import com.project.enums.NotificationStatus;
import com.project.repository.NotificationRepository;
import com.project.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImplementation implements NotificationService {

	@Autowired
	NotificationRepository repo;

	@Override
	public NotificationDto sendNotification(NotificationDto dto) {
		Notification e = Notification.builder()
				.userId(dto.getUserId())
				.message(dto.getMessage())
				.category(dto.getCategory())
				.status(NotificationStatus.Unread)
				.build();
		Notification res = repo.save(e);
		log.info("Notification {} sent to user {}", res.getNotificationId(), res.getUserId());
		return toDto(res);
	}

	@Override
	public List<NotificationDto> getByUser(Integer userId) {
		return toDtoList(repo.findByUserId(userId));
	}

	@Override
	public List<NotificationDto> getUnread(Integer userId) {
		return toDtoList(repo.findByUserIdAndStatus(userId, NotificationStatus.Unread));
	}

	@Override
	public NotificationDto markAsRead(Integer id) throws Exception {
		Notification e = repo.findById(id).orElseThrow(() -> new Exception("Notification not found with ID: " + id));
		e.setStatus(NotificationStatus.Read);
		Notification res = repo.save(e);
		log.info("Notification {} marked read", id);
		return toDto(res);
	}

	@Override
	public int markAllAsRead(Integer userId) {
		List<Notification> unread = repo.findByUserIdAndStatus(userId, NotificationStatus.Unread);
		for (Notification n : unread) {
			n.setStatus(NotificationStatus.Read);
		}
		repo.saveAll(unread);
		log.info("Marked {} notifications read for user {}", unread.size(), userId);
		return unread.size();
	}

	@Override
	public long getUnreadCount(Integer userId) {
		return repo.countByUserIdAndStatus(userId, NotificationStatus.Unread);
	}

	private List<NotificationDto> toDtoList(List<Notification> list) {
		List<NotificationDto> dtos = new ArrayList<>();
		for (Notification e : list) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	private NotificationDto toDto(Notification e) {
		NotificationDto dto = new NotificationDto();
		dto.setNotificationId(e.getNotificationId());
		dto.setUserId(e.getUserId());
		dto.setMessage(e.getMessage());
		dto.setCategory(e.getCategory());
		dto.setStatus(e.getStatus());
		dto.setCreatedDate(e.getCreatedDate());
		return dto;
	}
}
