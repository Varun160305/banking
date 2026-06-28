package com.project.service;

import java.util.List;

import com.project.dto.AmlFlagDto;
import com.project.enums.AmlStatus;

public interface AmlFlagService {

	AmlFlagDto raiseFlag(AmlFlagDto dto) throws Exception;

	AmlFlagDto investigateFlag(Integer id) throws Exception;

	AmlFlagDto clearFlag(Integer id) throws Exception;

	List<AmlFlagDto> getOpenFlags();

	List<AmlFlagDto> getByStatus(AmlStatus status);

	List<AmlFlagDto> getFlagsByAccount(Integer accountId);
}
