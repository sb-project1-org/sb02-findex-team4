package com.sprint.findex.sb02findexteam4.index.data.repository;

import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_BAD_REQUEST;

import com.sprint.findex.sb02findexteam4.exception.InvalidRequestException;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCsvExportCommand;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataFindCommand;
import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import com.sprint.findex.sb02findexteam4.index.data.entity.QIndexData;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;

@Repository
@RequiredArgsConstructor
public class IndexDataRepositoryImpl implements IndexDataRepositoryCustom {
  private final JPAQueryFactory queryFactory;
  private final QIndexData indexData = QIndexData.indexData;

  @Override
  public List<IndexData> findWithConditions(IndexDataFindCommand command) {
    BooleanBuilder builder = new BooleanBuilder();

    if (command.indexInfoId() != null) {
      builder.and(indexData.indexInfo.id.eq(command.indexInfoId()));
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    if (command.startDate() != null) {
      LocalDate startDate = LocalDate.parse(command.startDate(), formatter);
      builder.and(indexData.baseDate.goe(startDate.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()));
    }

    if (command.endDate() != null) {
      LocalDate endDate = LocalDate.parse(command.endDate(), formatter);
      builder.and(indexData.baseDate.loe(endDate.atTime(23, 59, 59).atZone(ZoneId.of("Asia/Seoul")).toInstant()));
    }

    if (command.idAfter() != null) {
      builder.and(indexData.id.gt(command.idAfter()));
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
        .limit(command.size() + 1)
        .fetch();
  }

  @Override
  public Long countWithConditions(IndexDataFindCommand command) {
    BooleanBuilder builder = new BooleanBuilder();

    if (command.indexInfoId() != null) {
      builder.and(indexData.indexInfo.id.eq(command.indexInfoId()));
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    if (command.startDate() != null) {
      LocalDate startDate = LocalDate.parse(command.startDate(), formatter);
      builder.and(indexData.baseDate.goe(startDate.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()));
    }

    if (command.endDate() != null) {
      LocalDate endDate = LocalDate.parse(command.endDate(), formatter);
      builder.and(indexData.baseDate.loe(endDate.atTime(23, 59, 59).atZone(ZoneId.of("Asia/Seoul")).toInstant()));
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    if (command.startDate() != null) {
      LocalDate startDate = LocalDate.parse(command.startDate(), formatter);
      builder.and(indexData.baseDate.goe(startDate.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()));
    }
    if (command.endDate() != null) {
      LocalDate endDate = LocalDate.parse(command.endDate(), formatter);
      builder.and(indexData.baseDate.loe(endDate.atTime(23, 59, 59).atZone(ZoneId.of("Asia/Seoul")).toInstant()));
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


