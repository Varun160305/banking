package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {

	List<Portfolio> findByCustomerId(Integer customerId);

	List<Portfolio> findByRelationshipManagerId(Integer relationshipManagerId);
}
