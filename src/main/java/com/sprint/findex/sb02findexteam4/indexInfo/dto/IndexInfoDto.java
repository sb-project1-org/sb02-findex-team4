package com.sprint.findex.sb02findexteam4.indexInfo.dto;

import com.sprint.findex.sb02findexteam4.indexInfo.entity.SourceType;
import java.time.ZonedDateTime;

public record IndexInfoDto(
    Long id,
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    ZonedDateTime basePointInTime,
    double baseIndex,
    SourceType sourceType,
    boolean favorite
) {
}