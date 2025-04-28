package com.sprint.findex.sb02findexteam4.sync.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.findex.sb02findexteam4.sync.dto.search.AutoSyncConfigCondition;
import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigDto;
import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseAutoSyncConfigDto;
import com.sprint.findex.sb02findexteam4.sync.entity.AutoSyncConfig;
import com.sprint.findex.sb02findexteam4.sync.entity.QAutoSyncConfig;
import com.sprint.findex.sb02findexteam4.util.CustomQuerydslSortUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomAutoSyncConfigRepositoryImpl implements CustomAutoSyncConfigRepository {

  private final JPAQueryFactory queryFactory;
  private final QAutoSyncConfig autoSyncConfig = QAutoSyncConfig.autoSyncConfig;

  @Override
  public CursorPageResponseAutoSyncConfigDto findAutoSyncConfig(AutoSyncConfigCondition condition) {
    List<BooleanExpression> conditions = buildConditions(condition);
    Pageable pageable = createPage(condition.sortDirection(), condition.sortField(),
        condition.size());

    List<AutoSyncConfig> result = queryFactory
        .selectFrom(autoSyncConfig)
        .where(conditions.toArray(new BooleanExpression[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1) // → hasNext 체크를 위해 +1 조회
        .orderBy(CustomQuerydslSortUtils.createOrderSpecifiersForAutoSyncConfig(autoSyncConfig,
            pageable))
        .fetch();

    boolean hasNext = result.size() > pageable.getPageSize();
    if (hasNext) {
      result.remove(result.size() - 1); // 초과분 제거
    }

    return createCursorPage(result, pageable, hasNext, condition.sortField());
  }


  private Pageable createPage(String sortDirection, String sortField, int size) {
    Sort.Direction Direction = Sort.Direction.fromString(sortDirection);
    Sort sort = Sort.by(Direction, sortField);
    return PageRequest.of(0, size, sort);
  }

  private List<BooleanExpression> buildConditions(AutoSyncConfigCondition c) {
    List<BooleanExpression> conditions = new ArrayList<>();

    if (c.indexInfoId() != null) {
      conditions.add(autoSyncConfig.indexInfo.id.eq(c.indexInfoId()));
    }
    if (c.enabled() != null) {
      conditions.add(c.enabled()
          ? autoSyncConfig.enabled.isTrue()
          : autoSyncConfig.enabled.isFalse());
    }

    if (c.cursor() != null) {
      BooleanExpression cursorCondition = buildCursorCondition(c);
      conditions.add(cursorCondition);
    }

    return conditions;
  }

  private CursorPageResponseAutoSyncConfigDto createCursorPage(List<AutoSyncConfig> result,
      Pageable pageable, boolean hasNext, String sortField) {
    String nextCursor = null;
    Long nextIdAfter = null;

    if (!result.isEmpty()) {
      AutoSyncConfig last = result.get(result.size() - 1);

      if ("indexInfo.indexName".equals(sortField)) {
        nextCursor = last.getIndexInfo().getIndexName();
      } else if ("enabled".equals(sortField)) {
        nextCursor = String.valueOf(last.isEnabled());
      }

      nextIdAfter = last.getId();
    }

    List<AutoSyncConfigDto> content = result.stream()
        .map(AutoSyncConfigDto::of).toList();

    return new CursorPageResponseAutoSyncConfigDto(content, nextCursor, nextIdAfter,
        pageable.getPageSize(), (long) content.size(), hasNext);
  }

  private BooleanExpression buildCursorCondition(AutoSyncConfigCondition c) {
    String sortField = c.sortField();
    String direction = c.sortDirection();
    String cursor = c.cursor();

    boolean isAsc = "asc".equalsIgnoreCase(direction);

    // 커서 기준 비교 생성
    return "indexInfo.indexName".equals(sortField)
        ? (isAsc ? autoSyncConfig.indexInfo.indexName.lt(cursor)
        : autoSyncConfig.indexInfo.indexName.gt(cursor))
        : (isAsc ? autoSyncConfig.enabled.lt(Boolean.valueOf(cursor)) : autoSyncConfig.enabled.gt(
            Boolean.valueOf(cursor)));

  }
}
