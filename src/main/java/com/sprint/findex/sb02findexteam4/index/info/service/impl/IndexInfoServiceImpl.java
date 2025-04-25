package com.sprint.findex.sb02findexteam4.index.info.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.NormalException;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.QIndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.index.info.repository.IndexInfoRepository;
import com.sprint.findex.sb02findexteam4.index.info.service.IndexInfoService;
import com.sprint.findex.sb02findexteam4.index.info.service.IndexInfoValidator;
import com.sprint.findex.sb02findexteam4.sync.service.AutoSyncConfigService;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

//  @Override
//  public Page<IndexInfoSummaryDto> getIndexInfoWithFilters() {
//  }

  @Override
  public List<IndexInfoSummaryDto> getIndexInfoSummaries() {
    QIndexInfo q = QIndexInfo.indexInfo;

    return queryFactory
        .select(Projections.constructor(
            IndexInfoSummaryDto.class,
            q.id,
            q.indexClassification,
            q.indexName
        ))
        .from(q)
        .orderBy(q.id.desc())
        .fetch();
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
