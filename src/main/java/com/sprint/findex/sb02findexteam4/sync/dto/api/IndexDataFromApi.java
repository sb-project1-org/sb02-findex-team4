package com.sprint.findex.sb02findexteam4.sync.dto.api;

import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;

@Builder
public record IndexDataFromApi(
    String indexClassification,
    String indexName,
    Instant baseDate,
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
        .baseDate(TimeUtils.formatedTimeInstantFromApi(item.basDt()))
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
