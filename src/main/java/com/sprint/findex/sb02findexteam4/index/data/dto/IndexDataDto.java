package com.sprint.findex.sb02findexteam4.index.data.dto;

import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import java.math.BigDecimal;

public record IndexDataDto(
    Long id,
    String baseDate,
    BigDecimal closingPrice,
    BigDecimal marketPrice,
    BigDecimal highPrice,
    BigDecimal lowPrice,
    BigDecimal versus,
    BigDecimal fluctuationRate,
    Long tradingQuantity,
    Long tradingPrice,
    Long marketTotalAmount
) {
  public static IndexDataDto from(IndexData entity) {
    return new IndexDataDto(
        entity.getId(),
        entity.getBaseDate().toString(),
        entity.getClosingPrice(),
        entity.getMarketPrice(),
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

