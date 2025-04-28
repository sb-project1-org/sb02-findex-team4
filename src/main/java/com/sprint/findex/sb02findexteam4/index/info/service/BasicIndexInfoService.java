package com.sprint.findex.sb02findexteam4.index.info.service;

import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.NormalException;
import com.sprint.findex.sb02findexteam4.index.info.dto.CursorPageResponseIndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSearchCondition;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.repository.IndexInfoRepository;
import com.sprint.findex.sb02findexteam4.sync.service.AutoSyncConfigService;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BasicIndexInfoService implements IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final AutoSyncConfigService autoSyncConfigService;

  public BasicIndexInfoService(IndexInfoRepository indexInfoRepository,
      AutoSyncConfigService autoSyncConfigService,
      EntityManager entityManager) {
    this.indexInfoRepository = indexInfoRepository;
    this.autoSyncConfigService = autoSyncConfigService;
  }

  @Override
  @Transactional
  public IndexInfoDto registerIndexInfo(IndexInfoCreateCommand command) {
    IndexInfo indexInfo = IndexInfo.create(command);
    indexInfoRepository.save(indexInfo);
    autoSyncConfigService.create(indexInfo);

    return IndexInfoDto.of(indexInfo);
  }

  @Override
  @Transactional
  public IndexInfo registerIndexInfoFromApi(IndexInfoCreateCommand command) {
    IndexInfo indexInfo = IndexInfo.create(command);
    indexInfoRepository.save(indexInfo);
    autoSyncConfigService.create(indexInfo);

    return indexInfo;
  }

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseIndexInfoDto findIndexInfoByCursor(IndexInfoSearchCondition condition) {
    return indexInfoRepository.findIndexInfo(condition);
  }

  @Override
  @Transactional(readOnly = true)
  public List<IndexInfoSummaryDto> getIndexInfoSummaries() {
    return indexInfoRepository.getIndexInfoSummaries();
  }

  @Override
  @Transactional(readOnly = true)
  public IndexInfoDto findById(Long id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new NormalException(ErrorCode.INDEX_INFO_NOT_FOUND));

    return IndexInfoDto.of(indexInfo);
  }

  @Override
  @Transactional
  public IndexInfoDto updateIndexInfo(Long id, IndexInfoUpdateRequest updateDto) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new NormalException(ErrorCode.INDEX_INFO_NOT_FOUND));

    if (updateDto.employedItemsCount() != null &&
        !updateDto.employedItemsCount().equals(indexInfo.getEmployedItemsCount())) {
      indexInfo.setEmployedItemsCount(updateDto.employedItemsCount());
    }

    if (updateDto.basePointInTime() != null &&
        !updateDto.basePointInTime().equals(indexInfo.getBasePointInTime())) {
      Instant basePointInTimeInstant = TimeUtils.formatedTimeInstant(updateDto.basePointInTime());
      indexInfo.setBasePointInTime(basePointInTimeInstant);
    }

    if (updateDto.baseIndex() != null &&
        !updateDto.baseIndex().equals(indexInfo.getBaseIndex())) {
      indexInfo.setBaseIndex(updateDto.baseIndex());
    }

    if (updateDto.favorite() != null &&
        !updateDto.favorite().equals(indexInfo.getFavorite())) {
      indexInfo.setFavorite(updateDto.favorite());
    }

    return IndexInfoDto.of(indexInfo);
  }

  @Override
  @Transactional
  public void deleteIndexInfo(Long id) {
    autoSyncConfigService.deleteByIndexInfoId(id);
    indexInfoRepository.deleteById(id);
  }
}
