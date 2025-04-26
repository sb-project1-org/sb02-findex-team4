package com.sprint.findex.sb02findexteam4.index.info.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.NormalException;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.index.info.repository.IndexInfoRepository;
import com.sprint.findex.sb02findexteam4.index.info.service.IndexInfoService;
import com.sprint.findex.sb02findexteam4.index.info.service.IndexInfoValidator;
import com.sprint.findex.sb02findexteam4.sync.service.AutoSyncConfigService;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class IndexInfoServiceImpl implements IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoValidator indexInfoValidator;
  private final AutoSyncConfigService autoSyncConfigService;
  private final JPAQueryFactory queryFactory;

  public IndexInfoServiceImpl(IndexInfoRepository indexInfoRepository,
      IndexInfoValidator indexInfoValidator,
      AutoSyncConfigService autoSyncConfigService,
      EntityManager entityManager) {
    this.indexInfoRepository = indexInfoRepository;
    this.indexInfoValidator = indexInfoValidator;
    this.autoSyncConfigService = autoSyncConfigService;
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  @Transactional
  public IndexInfoDto registerIndexInfo(IndexInfoCreateRequest requestDto, SourceType sourceType) {
    indexInfoValidator.validateForCreate(requestDto);

    IndexInfoCreateCommand command = IndexInfoCreateCommand.of(requestDto);

    IndexInfo indexInfo = IndexInfo.create(command, sourceType);
    indexInfoRepository.save(indexInfo);

    autoSyncConfigService.create(indexInfo);

    IndexInfoDto indexInfoDto = IndexInfoDto.of(indexInfo);
    return indexInfoDto;
  }

  @Override
  public IndexInfo registerIndexInfoFromApi(IndexInfoCreateCommand command) {
    indexInfoValidator.validateForCreate(command);

    SourceType sourceType = SourceType.OPEN_API;
    IndexInfo indexInfo = IndexInfo.create(command, sourceType);
    indexInfoRepository.save(indexInfo);

    autoSyncConfigService.create(indexInfo);

    return indexInfo;
  }

  @Override
  public Page<IndexInfoDto> getIndexInfoWithFilters(
      IndexInfoDto indexInfoDto,
      String sortProperty,
      boolean isAsc,
      Long cursorId,
      int pageSize) {

    return indexInfoRepository
        .getIndexInfoWithFilters(indexInfoDto, sortProperty, isAsc, cursorId, pageSize);
  }

  @Override
  public List<IndexInfoSummaryDto> getIndexInfoSummaries() {
    return indexInfoRepository.getIndexInfoSummaries();
  }

  @Override
  public IndexInfoDto findById(Long id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new NormalException(ErrorCode.INDEX_INFO_NOT_FOUND));

    IndexInfoDto indexInfoDto = IndexInfoDto.of(indexInfo);

    return indexInfoDto;
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
  public void deleteIndexInfo(Long id) {
    indexInfoRepository.deleteById(id);
    autoSyncConfigService.deleteByIndexInfoId(id);
  }
}
