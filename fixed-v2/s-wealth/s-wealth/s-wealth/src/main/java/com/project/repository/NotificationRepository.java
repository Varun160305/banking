package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.Notification;
import com.project.enums.NotificationStatus;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	List<Notification> findByUserId(Integer userId);

	List<Notification> findByUserIdAndStatus(Integer userId, NotificationStatus status);

	long countByUserIdAndStatus(Integer userId, NotificationStatus status);
}
