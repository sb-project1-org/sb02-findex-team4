package com.sprint.findex.sb02findexteam4.index.data.dto;

import com.sprint.findex.sb02findexteam4.index.data.entity.PeriodType;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import java.util.List;
import lombok.Builder;

@Builder
public record IndexChartDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    PeriodType periodType,
    List<ChartPoint> dataPoints,
    List<ChartPoint> ma5DataPoints,
    List<ChartPoint> ma20DataPoints
) {

  public static IndexChartDto from(IndexInfo indexInfo, PeriodType periodType,
      List<ChartPoint> dataPoints,
      List<ChartPoint> ma5DataPoints, List<ChartPoint> ma20DataPoints) {
    return IndexChartDto.builder()
        .indexInfoId(indexInfo.getId())
        .indexClassification(indexInfo.getIndexClassification())
        .indexName(indexInfo.getIndexName())
        .periodType(periodType)
        .dataPoints(dataPoints)
        .ma5DataPoints(ma5DataPoints)
        .ma20DataPoints(ma20DataPoints)
        .build();
  }
}