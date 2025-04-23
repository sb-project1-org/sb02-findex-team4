package com.sprint.findex.sb02findexteam4.indexData.dto;

import com.sprint.findex.sb02findexteam4.indexData.IndexData;
import com.sprint.findex.sb02findexteam4.indexInfo.SourceType;
import java.math.BigDecimal;
import java.time.Instant;

public record IndexDataCreateResponse(
    Long id,
    Long indexInfoId,
    Instant baseDate,
    SourceType sourceType,
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
  public static IndexDataCreateResponse from(IndexData entity) {
    return new IndexDataCreateResponse(
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
