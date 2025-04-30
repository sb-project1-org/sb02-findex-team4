package com.sprint.findex.sb02findexteam4.index.data.repository;

import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_BAD_REQUEST;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.findex.sb02findexteam4.exception.InvalidRequestException;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCsvExportCommand;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataSearchCondition;
import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import com.sprint.findex.sb02findexteam4.index.data.entity.QIndexData;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IndexDataRepositoryImpl implements IndexDataRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QIndexData indexData = QIndexData.indexData;

  @Override
  public List<IndexData> findWithConditions(IndexDataSearchCondition condition) {
    BooleanBuilder builder = new BooleanBuilder();

    if (condition.indexInfoId() != null) {
      builder.and(indexData.indexInfo.id.eq(condition.indexInfoId()));
    }

    if (condition.startDate() != null) {
      builder.and(indexData.baseDate.goe(condition.startDate()));
    }

    if (condition.endDate() != null) {
      builder.and(indexData.baseDate.loe(condition.endDate()));
    }

    if (condition.idAfter() != null) {
      builder.and(indexData.id.gt(condition.idAfter()));
    }

    OrderSpecifier<?> orderSpecifier;

    switch (condition.sortField()) {
      case "baseDate" -> orderSpecifier = "asc".equalsIgnoreCase(condition.sortDirection())
          ? indexData.baseDate.asc()
          : indexData.baseDate.desc();
      case "marketPrice" -> orderSpecifier = "asc".equalsIgnoreCase(condition.sortDirection())
          ? indexData.marketPrice.asc()
          : indexData.marketPrice.desc();
      case "closingPrice" -> orderSpecifier = "asc".equalsIgnoreCase(condition.sortDirection())
          ? indexData.closingPrice.asc()
          : indexData.closingPrice.desc();
      case "highPrice" -> orderSpecifier = "asc".equalsIgnoreCase(condition.sortDirection())
          ? indexData.highPrice.asc()
          : indexData.highPrice.desc();
      case "lowPrice" -> orderSpecifier = "asc".equalsIgnoreCase(condition.sortDirection())
          ? indexData.lowPrice.asc()
          : indexData.lowPrice.desc();
      case "versus" -> orderSpecifier = "asc".equalsIgnoreCase(condition.sortDirection())
          ? indexData.versus.asc()
          : indexData.versus.desc();
      case "fluctuationRate" -> orderSpecifier = "asc".equalsIgnoreCase(condition.sortDirection())
          ? indexData.fluctuationRate.asc()
          : indexData.fluctuationRate.desc();
      case "tradingQuantity" -> orderSpecifier = "asc".equalsIgnoreCase(condition.sortDirection())
          ? indexData.tradingQuantity.asc()
          : indexData.tradingQuantity.desc();
      case "tradingPrice" -> orderSpecifier = "asc".equalsIgnoreCase(condition.sortDirection())
          ? indexData.tradingPrice.asc()
          : indexData.tradingPrice.desc();
      case "marketTotalAmount" -> orderSpecifier = "asc".equalsIgnoreCase(condition.sortDirection())
          ? indexData.marketTotalAmount.asc()
          : indexData.marketTotalAmount.desc();

      default -> throw new InvalidRequestException(INDEX_DATA_BAD_REQUEST);
    }

    return queryFactory
        .selectFrom(indexData)
        .where(builder)
        .orderBy(orderSpecifier)
        .limit(condition.size() + 1)
        .fetch();
  }

  @Override
  public Long countWithConditions(IndexDataSearchCondition command) {
    BooleanBuilder builder = new BooleanBuilder();

    if (command.indexInfoId() != null) {
      builder.and(indexData.indexInfo.id.eq(command.indexInfoId()));
    }

    if (command.startDate() != null) {
      builder.and(indexData.baseDate.goe(command.startDate()));
    }

    if (command.endDate() != null) {
      builder.and(indexData.baseDate.loe(command.endDate()));
    }

    return queryFactory
        .select(indexData.count())
        .from(indexData)
        .where(builder)
        .fetchOne();
  }

  @Override
  public List<IndexData> findAllForCsvExport(IndexDataCsvExportCommand command) {
    BooleanBuilder builder = new BooleanBuilder();

    if (command.indexInfoId() != null) {
      builder.and(indexData.indexInfo.id.eq(command.indexInfoId()));
    }

    if (command.startDate() != null) {
      builder.and(indexData.baseDate.goe(command.startDate()));
    }

    if (command.endDate() != null) {
      builder.and(indexData.baseDate.loe(command.endDate()));
    }

    OrderSpecifier<?> orderSpecifier;

    switch (command.sortField()) {
      case "baseDate" -> orderSpecifier = "asc".equalsIgnoreCase(command.sortDirection())
          ? indexData.baseDate.asc()
          : indexData.baseDate.desc();
      case "marketPrice" -> orderSpecifier = "asc".equalsIgnoreCase(command.sortDirection())
          ? indexData.marketPrice.asc()
          : indexData.marketPrice.desc();
      case "closingPrice" -> orderSpecifier = "asc".equalsIgnoreCase(command.sortDirection())
          ? indexData.closingPrice.asc()
          : indexData.closingPrice.desc();
      case "highPrice" -> orderSpecifier = "asc".equalsIgnoreCase(command.sortDirection())
          ? indexData.highPrice.asc()
          : indexData.highPrice.desc();
      case "lowPrice" -> orderSpecifier = "asc".equalsIgnoreCase(command.sortDirection())
          ? indexData.lowPrice.asc()
          : indexData.lowPrice.desc();
      case "versus" -> orderSpecifier = "asc".equalsIgnoreCase(command.sortDirection())
          ? indexData.versus.asc()
          : indexData.versus.desc();
      case "fluctuationRate" -> orderSpecifier = "asc".equalsIgnoreCase(command.sortDirection())
          ? indexData.fluctuationRate.asc()
          : indexData.fluctuationRate.desc();
      case "tradingQuantity" -> orderSpecifier = "asc".equalsIgnoreCase(command.sortDirection())
          ? indexData.tradingQuantity.asc()
          : indexData.tradingQuantity.desc();
      case "tradingPrice" -> orderSpecifier = "asc".equalsIgnoreCase(command.sortDirection())
          ? indexData.tradingPrice.asc()
          : indexData.tradingPrice.desc();
      case "marketTotalAmount" -> orderSpecifier = "asc".equalsIgnoreCase(command.sortDirection())
          ? indexData.marketTotalAmount.asc()
          : indexData.marketTotalAmount.desc();

      default -> throw new InvalidRequestException(INDEX_DATA_BAD_REQUEST);
    }

    return queryFactory
        .selectFrom(indexData)
        .where(builder)
        .orderBy(orderSpecifier)
        .fetch();
  }
}


