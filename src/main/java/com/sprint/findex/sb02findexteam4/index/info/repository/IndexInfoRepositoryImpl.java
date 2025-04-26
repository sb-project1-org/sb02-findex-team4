package com.sprint.findex.sb02findexteam4.index.info.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.QIndexInfo;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

public class IndexInfoRepositoryImpl implements IndexInfoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public IndexInfoRepositoryImpl(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  public Page<IndexInfoDto> getIndexInfoWithFilters(
      IndexInfoDto indexInfoDto,
      String sortProperty,
      boolean isAsc,
      Long cursorId,
      int pageSize) {

    QIndexInfo q = QIndexInfo.indexInfo;
    BooleanBuilder builder = new BooleanBuilder();

    if (indexInfoDto.indexClassification() != null && !indexInfoDto.indexClassification().isBlank()) {
      builder.and(q.indexClassification.containsIgnoreCase(indexInfoDto.indexClassification()));
    }

    if (indexInfoDto.indexName() != null && !indexInfoDto.indexName().isBlank()) {
      builder.and(q.indexName.containsIgnoreCase(indexInfoDto.indexName()));
    }

    if (indexInfoDto.favorite() != null) {
      builder.and(q.favorite.eq(indexInfoDto.favorite()));
    }

    OrderSpecifier<?> sortOrder;
    switch (sortProperty) {
      case "indexClassification" -> sortOrder = isAsc ? q.indexClassification.asc() : q.indexClassification.desc();
      case "indexName" -> sortOrder = isAsc ? q.indexName.asc() : q.indexName.desc();
      case "employedItemsCount" -> sortOrder = isAsc ? q.employedItemsCount.asc() : q.employedItemsCount.desc();
      default -> sortOrder = isAsc ? q.id.asc() : q.id.desc();
    }

    if (cursorId != null) {
      builder.and(isAsc ? q.id.gt(cursorId) : q.id.lt(cursorId));
    }

    List<IndexInfo> result = queryFactory
        .selectFrom(q)
        .where(builder)
        .orderBy(sortOrder)
        .limit(pageSize + 1)
        .fetch();

    boolean hasNext = result.size() > pageSize;
    if (hasNext) {
      result.remove(pageSize);
    }

    List<IndexInfoDto> dtoList = result.stream()
        .map(IndexInfoDto::of)
        .collect(Collectors.toList());

    return new PageImpl<>(dtoList, Pageable.ofSize(pageSize), hasNext ? pageSize + 1 : dtoList.size());
  }

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
}