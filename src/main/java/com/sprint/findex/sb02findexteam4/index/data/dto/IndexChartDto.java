package com.sprint.findex.sb02findexteam4.index.data.dto;

import com.sprint.findex.sb02findexteam4.index.data.entity.PeriodType;
import java.util.List;

public record IndexChartDto(
    Long indexInfoId,
    String indexClassification,
    String indexName,
    PeriodType periodType,
    List<ChartPoint> dataPoints,
    List<ChartPoint> ma5DataPoints,
    List<ChartPoint> ma20DataPoints
) {}