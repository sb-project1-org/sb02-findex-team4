package com.sprint.findex.sb02findexteam4.sync.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.NormalException;
import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseSyncJobDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobSearchCondition;
import com.sprint.findex.sb02findexteam4.sync.entity.QSyncJobHistory;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.sync.mapper.SyncJobHistoryMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomSyncJobHistoryRepositoryImpl implements CustomSyncJobHistoryRepository {

    private final JPAQueryFactory queryFactory;
    private final QSyncJobHistory syncJobHistory = QSyncJobHistory.syncJobHistory;

    @Override
    public CursorPageResponseSyncJobDto findSyncJobs(SyncJobSearchCondition condition) {
        List<BooleanExpression> conditions = buildConditions(condition);
        BooleanExpression cursorPredicate = getCursorPredicate(condition);
        if(cursorPredicate != null) conditions.add(cursorPredicate);

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(condition.sortField(), condition.sortDirection());

        List<SyncJobHistory> result = queryFactory
            .selectFrom(syncJobHistory)
            .where(conditions.toArray(BooleanExpression[]::new))
            .orderBy(orderSpecifier)
            .limit(condition.size() + 1)
            .fetch();

        boolean hasNext = result.size() > condition.size();
        if (hasNext) result.remove(result.size() - 1);

        List<SyncJobHistoryDto> content = result.stream()
            .map(SyncJobHistoryMapper::toDto)
            .toList();

        List<BooleanExpression> countConditions = buildConditions(condition);
        Long totalCount = queryFactory
            .select(syncJobHistory.count())
            .from(syncJobHistory)
            .where(countConditions.toArray(BooleanExpression[]::new))
            .fetchOne();

        return SyncJobHistoryMapper.toCursorPageResponseDto(content, hasNext, totalCount);
    }

    private List<BooleanExpression> buildConditions(SyncJobSearchCondition c) {
        List<BooleanExpression> conditions = new ArrayList<>();

        if (c.jobType() != null) conditions.add(syncJobHistory.jobType.eq(c.jobType()));
        if (c.indexInfoId() != null) conditions.add(syncJobHistory.indexInfo.id.eq(c.indexInfoId()));
        if (c.baseDateFrom() != null) conditions.add(syncJobHistory.targetDate.goe(c.baseDateFrom()));
        if (c.baseDateTo() != null) conditions.add(syncJobHistory.targetDate.loe(c.baseDateTo()));
        if (c.worker() != null) conditions.add(syncJobHistory.worker.eq(c.worker()));
        if (c.jobTimeFrom() != null) conditions.add(syncJobHistory.jobTime.goe(c.jobTimeFrom()));
        if (c.jobTimeTo() != null) conditions.add(syncJobHistory.jobTime.loe(c.jobTimeTo()));
        if (c.status() != null) conditions.add(syncJobHistory.jobResult.eq(c.status()));

        return conditions;
    }

    private BooleanExpression getCursorPredicate(SyncJobSearchCondition condition) {
        String sortField = condition.sortField();
        String direction = condition.sortDirection();
        String cursor = condition.cursor();

        if (sortField == null || direction == null){
            throw new NormalException(ErrorCode.INDEX_INFO_BAD_REQUEST);
        }

        if (cursor == null) return null;

        Instant cursorValue = Instant.parse(cursor);
        boolean isDesc = "desc".equalsIgnoreCase(direction);

        return "jobTime".equals(sortField)
            ? (isDesc ? syncJobHistory.jobTime.lt(cursorValue) : syncJobHistory.jobTime.gt(cursorValue))
            : (isDesc ? syncJobHistory.targetDate.lt(cursorValue) : syncJobHistory.targetDate.gt(cursorValue));
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortField, String sortDirection) {
        Order order = "desc".equalsIgnoreCase(sortDirection) ? Order.DESC : Order.ASC;

        return "jobTime".equals(sortField)
            ? new OrderSpecifier<>(order, syncJobHistory.jobTime)
            : new OrderSpecifier<>(order, syncJobHistory.targetDate);
    }
}