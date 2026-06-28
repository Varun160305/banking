package com.project.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.project.enums.ReportScope;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banking_report")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankingReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reportId;

	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private ReportScope scope;
	@Column(columnDefinition = "TEXT")
	private String metrics;

	@CreationTimestamp
	private LocalDateTime generatedDate;
}
