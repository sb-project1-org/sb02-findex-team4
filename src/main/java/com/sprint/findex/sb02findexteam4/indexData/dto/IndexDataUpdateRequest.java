package com.sprint.findex.sb02findexteam4.indexData.dto;

import java.math.BigDecimal;

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
}
