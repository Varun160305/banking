package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.project.entities.User;
import com.project.enums.AuditAction;
import com.project.enums.AuditModule;
import com.project.repository.UserRepository;

/**
 * Convenience wrapper used by the business modules. Resolves the acting user and
 * IP, then delegates to the shared {@link AuditLogService#logAction}. Logging is
 * best-effort: a failure here must never break the business operation.
 */
@Service
public class AuditTrailService {

	@Autowired
	private AuditLogService auditLogService;

	@Autowired
	private UserRepository userRepo;

	/** Record an action performed by the currently authenticated user (resolved from the JWT). */
	public void record(AuditAction action, AuditModule module) {
		record(resolveCurrentUser(), action, module);
	}

	/** Record an action for an explicitly known user (e.g. login, where the security context is not yet populated). */
	public void record(User user, AuditAction action, AuditModule module) {
		try {
			Integer userId = (user == null) ? null : user.getUserId();
			auditLogService.logAction(userId, action.name(), module.name(), resolveIp(), null);
		} catch (Exception ignored) {
			// auditing must never break the primary operation
		}
	}

	private User resolveCurrentUser() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
				return userRepo.findByEmail(auth.getName()).orElse(null);
			}
		} catch (Exception ignored) {
		}
		return null;
	}

	private String resolveIp() {
		try {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				return attrs.getRequest().getRemoteAddr();
			}
		} catch (Exception ignored) {
		}
		return "N/A";
	}
}
