package com.sprint.findex.sb02findexteam4.index.data.dto;

import java.util.List;

public record IndexChartDto(
    List<ChartPoint> closingPrices,
    List<ChartPoint> ma5DataPoints,
    List<ChartPoint> ma20DataPoints
) {}