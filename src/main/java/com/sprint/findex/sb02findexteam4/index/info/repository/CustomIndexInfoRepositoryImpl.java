package com.sprint.findex.sb02findexteam4.index.info.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.findex.sb02findexteam4.index.info.dto.CursorPageResponseIndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSearchCondition;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.QIndexInfo;
import com.sprint.findex.sb02findexteam4.util.CustomQuerydslSortUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomIndexInfoRepositoryImpl implements CustomIndexInfoRepository {

  private final JPAQueryFactory queryFactory;
  private final QIndexInfo indexInfo = QIndexInfo.indexInfo;

  @Override
  public CursorPageResponseIndexInfoDto findIndexInfo(IndexInfoSearchCondition condition) {
    List<BooleanExpression> conditions = buildConditions(condition);
    if (condition.cursor() != null) {
      BooleanExpression cursorCondition = gerCursorPredicate(condition);
      conditions.add(cursorCondition);
    }
    
    Pageable pageable = createPage(condition.sortDirection(), condition.sortField(),
        condition.size());

    // 1. 데이터 조회 (size + 1개 조회해서 hasNext 판단)
    List<IndexInfo> result = queryFactory
        .selectFrom(indexInfo)
        .where(conditions.toArray(BooleanExpression[]::new))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1) // → hasNext 체크를 위해 +1 조회
        .orderBy(CustomQuerydslSortUtils.createOrderSpecifiers(indexInfo, pageable))
        .fetch();

    // 2. Slice 구성
    boolean hasNext = result.size() > pageable.getPageSize();
    if (hasNext) {
      result.remove(result.size() - 1); // 초과분 제거
    }

    List<IndexInfoDto> content = result.stream()
        .map(IndexInfoDto::of)   // 반드시 변환 필요! (IndexInfo → IndexInfoDto)
        .toList();

    List<BooleanExpression> countConditions = buildConditions(condition);
    Long totalCount = queryFactory
        .select(indexInfo.count())
        .from(indexInfo)
        .where(countConditions.toArray(BooleanExpression[]::new))
        .fetchOne();

    // 3. 커서 생성
    return createCursorPage(content, pageable, hasNext, condition.sortField(), totalCount);
  }

  private Pageable createPage(String sortDirection, String sortField, int size) {
    Sort.Direction Direction = Sort.Direction.fromString(sortDirection);
    Sort sort = Sort.by(Direction, sortField);
    return PageRequest.of(0, size, sort);
  }

  private List<BooleanExpression> buildConditions(IndexInfoSearchCondition c) {
    List<BooleanExpression> conditions = new ArrayList<>();

    if (c.indexClassification() != null && !c.indexClassification().isBlank()) {
      conditions.add(indexInfo.indexClassification.containsIgnoreCase(c.indexClassification()));
    }
    if (c.indexName() != null && !c.indexName().isBlank()) {
      conditions.add(indexInfo.indexName.containsIgnoreCase(c.indexName()));
    }
    if (c.favorite() != null) {
      conditions.add(c.favorite()
          ? indexInfo.favorite.isTrue()
          : indexInfo.favorite.isFalse());
    }

    return conditions;
  }

  private CursorPageResponseIndexInfoDto createCursorPage(List<IndexInfoDto> content,
      Pageable pageable, boolean hasNext, String sortField, Long totalCount) {
    String nextCursor = null;
    Long nextIdAfter = null;

    if (!content.isEmpty()) {
      IndexInfoDto last = content.get(content.size() - 1);

      if ("indexClassification".equals(sortField)) {
        nextCursor = last.indexClassification();
      } else if ("indexName".equals(sortField)) {
        nextCursor = last.indexName();
      } else if ("employedItemsCount".equals(sortField)) {
        nextCursor = String.valueOf(last.employedItemsCount());
      }

      nextIdAfter = last.id();
    }

    return new CursorPageResponseIndexInfoDto(
        content,
        nextCursor,
        nextIdAfter,
        pageable.getPageSize(),
        totalCount,
        hasNext
    );
  }

  private BooleanExpression gerCursorPredicate(IndexInfoSearchCondition c) {
    String sortField = c.sortField();
    String direction = c.sortDirection();
    String cursor = c.cursor();
    Long idAfter = c.idAfter();

    boolean isAsc = "asc".equalsIgnoreCase(direction);

    // 커서 기준 비교 생성
    if ("indexClassification".equals(sortField)) {
      return isAsc
          ? indexInfo.indexClassification.gt(cursor)
          .or(indexInfo.indexClassification.eq(cursor).and(indexInfo.id.gt(idAfter)))
          : indexInfo.indexClassification.lt(cursor)
              .or(indexInfo.indexClassification.eq(cursor).and(indexInfo.id.lt(idAfter)));
    }

    if ("indexName".equals(sortField)) {
      return isAsc
          ? indexInfo.indexName.gt(cursor)
          .or(indexInfo.indexName.eq(cursor).and(indexInfo.id.gt(idAfter)))
          : indexInfo.indexName.lt(cursor)
              .or(indexInfo.indexName.eq(cursor).and(indexInfo.id.lt(idAfter)));
    }

    if ("employedItemsCount".equals(sortField)) {
      Integer parsedCursor = Integer.parseInt(cursor);
      return isAsc
          ? indexInfo.employedItemsCount.gt(parsedCursor)
          .or(indexInfo.employedItemsCount.eq(parsedCursor).and(indexInfo.id.gt(idAfter)))
          : indexInfo.employedItemsCount.lt(parsedCursor)
              .or(indexInfo.employedItemsCount.eq(parsedCursor).and(indexInfo.id.lt(idAfter)));
    }

    // sortField 잘못된 경우
    throw new IllegalArgumentException("지원하지 않는 정렬 필드: " + sortField);
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

    if (indexInfoDto.indexClassification() != null && !indexInfoDto.indexClassification()
        .isBlank()) {
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
      case "indexClassification" ->
          sortOrder = isAsc ? q.indexClassification.asc() : q.indexClassification.desc();
      case "indexName" -> sortOrder = isAsc ? q.indexName.asc() : q.indexName.desc();
      case "employedItemsCount" ->
          sortOrder = isAsc ? q.employedItemsCount.asc() : q.employedItemsCount.desc();
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

    return new PageImpl<>(dtoList, Pageable.ofSize(pageSize),
        hasNext ? pageSize + 1 : dtoList.size());
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