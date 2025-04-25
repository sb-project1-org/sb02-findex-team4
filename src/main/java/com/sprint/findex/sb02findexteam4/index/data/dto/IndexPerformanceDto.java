package com.sprint.findex.sb02findexteam4.index.data.dto;

import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record IndexPerformanceDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    BigDecimal versus,
    BigDecimal fluctuationRate,
    BigDecimal currentPrice,
    BigDecimal beforePrice
) {
  public static IndexPerformanceDto of(IndexInfo info, IndexData current, IndexData before) {
    if (current == null || before == null) return null;

    BigDecimal versus = current.getClosingPrice().subtract(before.getClosingPrice());
    BigDecimal fluctuationRate = versus.divide(before.getClosingPrice(), 4, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100));

    return new IndexPerformanceDto(
        info.getId(),
        info.getIndexClassification(),
        info.getIndexName(),
        versus,
        fluctuationRate,
        current.getClosingPrice(),
        before.getClosingPrice()
    );
  }
}
