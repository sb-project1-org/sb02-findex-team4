package com.sprint.findex.sb02findexteam4.index.data.dto;

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
