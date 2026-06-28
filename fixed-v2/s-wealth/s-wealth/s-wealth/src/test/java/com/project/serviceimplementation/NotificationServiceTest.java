package com.project.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.dto.NotificationDto;
import com.project.entities.Notification;
import com.project.enums.NotificationCategory;
import com.project.enums.NotificationStatus;
import com.project.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

	@Mock
	NotificationRepository repo;

	@InjectMocks
	NotificationServiceImplementation service;

	@Test
	void sendNotification_unread() {
		when(repo.save(any(Notification.class))).thenAnswer(inv -> {
			Notification n = inv.getArgument(0);
			if (n.getNotificationId() == null) {
				n.setNotificationId(1);
			}
			return n;
		});

		NotificationDto dto = new NotificationDto();
		dto.setUserId(2);
		dto.setMessage("Hello");
		dto.setCategory(NotificationCategory.Transaction);

		NotificationDto result = service.sendNotification(dto);

		assertEquals(NotificationStatus.Unread, result.getStatus());
	}

	@Test
	void markAsRead_changes() throws Exception {
		Notification n = Notification.builder().notificationId(5).userId(2).status(NotificationStatus.Unread).build();
		when(repo.findById(5)).thenReturn(Optional.of(n));
		when(repo.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

		NotificationDto result = service.markAsRead(5);

		assertEquals(NotificationStatus.Read, result.getStatus());
	}

	@Test
	void getUnreadCount_correct() {
		when(repo.countByUserIdAndStatus(2, NotificationStatus.Unread)).thenReturn(3L);
		assertEquals(3L, service.getUnreadCount(2));
	}

	@Test
	void markAllAsRead_updatesAll() {
		Notification n1 = Notification.builder().notificationId(1).userId(2).status(NotificationStatus.Unread).build();
		Notification n2 = Notification.builder().notificationId(2).userId(2).status(NotificationStatus.Unread).build();
		when(repo.findByUserIdAndStatus(2, NotificationStatus.Unread)).thenReturn(List.of(n1, n2));

		int updated = service.markAllAsRead(2);

		assertEquals(2, updated);
		assertEquals(NotificationStatus.Read, n1.getStatus());
		assertEquals(NotificationStatus.Read, n2.getStatus());
	}
}
