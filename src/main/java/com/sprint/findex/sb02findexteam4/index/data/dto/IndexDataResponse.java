package com.sprint.findex.sb02findexteam4.index.data.dto;

import com.sprint.findex.sb02findexteam4.index.data.IndexData;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.math.BigDecimal;

public record IndexDataResponse(
    Long id,
    Long indexInfoId,
    String baseDate,
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
  public static IndexDataResponse from(IndexData entity) {
    String string = TimeUtils.formatedTimeString(entity.getBaseDate());

    return new IndexDataResponse(
        entity.getId(),
        entity.getIndexInfo().getId(),
        string,
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
