package com.sprint.findex.sb02findexteam4.indexData.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record IndexDataCreateRequest(
    Long indexInfoId,
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

}
