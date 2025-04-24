package com.sprint.findex.sb02findexteam4.indexData.dto;

import com.sprint.findex.sb02findexteam4.indexData.IndexData;
import java.math.BigDecimal;
import java.time.Instant;

public record IndexDataResponse(
    Long id,
    Long indexInfoId,
    Instant baseDate,
    com.sprint.findex.sb02findexteam4.indexInfo.entity.SourceType sourceType,
    BigDecimal marketPrice,
    BigDecimal closingPrice,
    BigDecimal highPrice,
    BigDecimal lowPrice,
    BigDecimal versus,
    BigDecimal fluctuationRate,
    Long tradingQuantity,
    Long tradingPrice,
    Long marketTotalAmount
) {
  public static IndexDataResponse from(IndexData entity) {
    return new IndexDataResponse(
        entity.getId(),
        entity.getIndexInfo().getId(),
        entity.getBaseDate(),
        entity.getSourceType(),
        entity.getMarketPrice(),
        entity.getClosingPrice(),
        entity.getHighPrice(),
        entity.getLowPrice(),
        entity.getVersus(),
        entity.getFluctuationRate(),
        entity.getTradingQuantity(),
        entity.getTradingPrice(),
        entity.getMarketTotalAmount()
    );
  }
}
