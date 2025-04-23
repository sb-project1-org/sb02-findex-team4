package com.sprint.findex.sb02findexteam4.indexInfo.dto;

import com.sprint.findex.sb02findexteam4.indexInfo.entity.SourceType;
import java.time.Instant;

public record IndexInfoDto(
    Long id,
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    Instant basePointInTime,
    double baseIndex,
    SourceType sourceType,
    boolean favorite
) {
}