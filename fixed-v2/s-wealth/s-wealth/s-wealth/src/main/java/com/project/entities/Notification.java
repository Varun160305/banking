package com.project.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.project.enums.NotificationCategory;
import com.project.enums.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notificationId;

	private Integer userId;
	@Column(columnDefinition = "TEXT")
	private String message;
	@Enumerated(EnumType.STRING)
	private NotificationCategory category;
	@Enumerated(EnumType.STRING)
	private NotificationStatus status;

	@CreationTimestamp
	private LocalDateTime createdDate;
}
