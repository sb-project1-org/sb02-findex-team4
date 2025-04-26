package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.exception.AlreadyExistsException;
import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.InvalidRequestException;
import com.sprint.findex.sb02findexteam4.exception.NotFoundException;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigCondition;
import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigDto;
import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigUpdateCommand;
import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseAutoSyncConfigDto;
import com.sprint.findex.sb02findexteam4.sync.entity.AutoSyncConfig;
import com.sprint.findex.sb02findexteam4.sync.repository.AutoSyncConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAutoSyncConfigService implements AutoSyncConfigService {

  private final AutoSyncConfigRepository autoSyncConfigRepository;

  @Override
  @Transactional
  public AutoSyncConfigDto create(IndexInfo indexInfo) {
    if (autoSyncConfigRepository.existsByIndexInfo_Id(indexInfo.getId())) {
      throw new AlreadyExistsException(ErrorCode.AUTO_SYNC_ALREADY_EXISTS);
    }
    try {

      AutoSyncConfig autoSyncConfig = AutoSyncConfig.create(indexInfo);
      autoSyncConfigRepository.save(autoSyncConfig);
      log.info("AutoSyncConfig Create {}", indexInfo);
      return AutoSyncConfigDto.of(autoSyncConfig);
    } catch (IllegalArgumentException e) {
      throw new InvalidRequestException(ErrorCode.AUTO_SYNC_BAD_REQUEST);
    }
  }

  @Override
  @Transactional
  public AutoSyncConfigDto create(IndexInfo indexInfo, boolean enabled) {
    if (autoSyncConfigRepository.existsByIndexInfo_Id(indexInfo.getId())) {
      throw new AlreadyExistsException(ErrorCode.AUTO_SYNC_ALREADY_EXISTS);
    }
    try {
      AutoSyncConfig autoSyncConfig = AutoSyncConfig.create(indexInfo, enabled);
      autoSyncConfigRepository.save(autoSyncConfig);
      log.info("AutoSyncConfig Create {} {}", indexInfo, enabled);
      return AutoSyncConfigDto.of(autoSyncConfig);
    } catch (IllegalArgumentException e) {
      throw new InvalidRequestException(ErrorCode.AUTO_SYNC_BAD_REQUEST);
    }
  }

  @Override
  @Transactional
  public AutoSyncConfigDto update(
      AutoSyncConfigUpdateCommand command) {
    try {
      //autoSyncConfig 있는지 확인, 없으면 not found 에러 발생
      AutoSyncConfig autoSyncConfig = autoSyncConfigRepository.findById(command.id()).orElseThrow(
          () -> new NotFoundException(ErrorCode.AUTO_SYNC_NOT_FOUND)
      );
      //enabled 상태를 업데이트함
      autoSyncConfig.update(command.enabled());
      log.info("AutoSyncConfig Update: {}", command.enabled());
      //build 패턴을 이용해서 dto 감싸기
      return AutoSyncConfigDto.of(autoSyncConfig);

      //예상치 못한 인수가 들어오면 bad request 에러 발생
    } catch (IllegalArgumentException e) {
      throw new InvalidRequestException(ErrorCode.AUTO_SYNC_BAD_REQUEST);
    }
  }

  @Override
  public CursorPageResponseAutoSyncConfigDto findAll(AutoSyncConfigCondition condition) {
    return autoSyncConfigRepository.findAutoSyncConfig(condition);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    try {
      autoSyncConfigRepository.deleteById(id);
    } catch (IllegalArgumentException e) {
      throw new NotFoundException(ErrorCode.AUTO_SYNC_NOT_FOUND);
    }
  }

  @Override
  @Transactional
  public void deleteByIndexInfoId(Long indexInfoId) {
    try {
      autoSyncConfigRepository.deleteByIndexInfo_Id(indexInfoId);
    } catch (IllegalArgumentException e) {
      throw new NotFoundException(ErrorCode.AUTO_SYNC_NOT_FOUND);
    }
  }
}
