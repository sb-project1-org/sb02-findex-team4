package com.sprint.findex.sb02findexteam4.index.data.dto;

import com.sprint.findex.sb02findexteam4.sync.dto.api.IndexDataFromApi;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record IndexDataCreateRequest(
    Long indexInfoId,
    String baseDate,
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

  public static IndexDataCreateRequest from(Long indexInfoId, IndexDataFromApi dataFromApi) {
    return IndexDataCreateRequest.builder()
        .indexInfoId(indexInfoId)
        .baseDate(TimeUtils.formatedTimeString(dataFromApi.baseDate()))
        .marketPrice(dataFromApi.marketPrice())
        .closingPrice(dataFromApi.closingPrice())
        .highPrice(dataFromApi.highPrice())
        .lowPrice(dataFromApi.lowPrice())
        .versus(dataFromApi.versus())
        .fluctuationRate(dataFromApi.fluctuationRate())
        .tradingQuantity(dataFromApi.tradingQuantity())
        .tradingPrice(dataFromApi.tradingPrice())
        .marketTotalAmount(dataFromApi.marketTotalAmount()).build();
  }
}
