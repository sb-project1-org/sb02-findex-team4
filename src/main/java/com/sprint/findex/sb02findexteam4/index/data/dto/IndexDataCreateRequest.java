package com.sprint.findex.sb02findexteam4.index.data.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record IndexDataCreateRequest(
    Long indexInfoId,
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

}
