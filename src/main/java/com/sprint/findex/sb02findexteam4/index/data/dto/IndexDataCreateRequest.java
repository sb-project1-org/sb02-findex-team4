package com.sprint.findex.sb02findexteam4.index.data.dto;

import java.math.BigDecimal;

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

}
