package com.sprint.findex.sb02findexteam4.sync.dto.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record IndexDataFromApi(
    String indexClassification,
    String indexName,
    LocalDate baseDate,
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

  public static IndexDataFromApi of(Item item) {
    return IndexDataFromApi.builder()
        .indexClassification(item.idxCsf())
        .indexName(item.idxNm())
        .baseDate(item.basDt())
        .marketPrice(item.mkp())
        .closingPrice(item.clpr())
        .highPrice(item.hipr())
        .lowPrice(item.lopr())
        .versus(item.vs())
        .fluctuationRate(item.fltRt())
        .tradingQuantity(item.trqu())
        .tradingPrice(item.trPrc())
        .marketTotalAmount(item.lstgMrktTotAmt())
        .build();
  }

}
