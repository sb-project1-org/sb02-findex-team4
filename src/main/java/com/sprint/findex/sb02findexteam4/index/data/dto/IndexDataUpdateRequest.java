package com.sprint.findex.sb02findexteam4.index.data.dto;

import com.sprint.findex.sb02findexteam4.sync.dto.api.IndexDataFromApi;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record IndexDataUpdateRequest(
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

  public static IndexDataUpdateRequest fromApi(IndexDataFromApi dataFromApi) {
    return IndexDataUpdateRequest.builder()
        .marketPrice(dataFromApi.marketPrice())
        .closingPrice(dataFromApi.closingPrice())
        .highPrice(dataFromApi.highPrice())
        .lowPrice(dataFromApi.lowPrice())
        .versus(dataFromApi.versus())
        .fluctuationRate(dataFromApi.fluctuationRate())
        .tradingPrice(dataFromApi.tradingPrice())
        .marketTotalAmount(dataFromApi.marketTotalAmount())
        .build();
  }
}
