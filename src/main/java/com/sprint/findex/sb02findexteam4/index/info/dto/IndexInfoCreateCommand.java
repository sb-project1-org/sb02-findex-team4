package com.sprint.findex.sb02findexteam4.index.info.dto;

import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.time.Instant;
import lombok.Builder;

@Builder
public record IndexInfoCreateCommand (
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    Instant basePointInTime,
    Double baseIndex,
    Boolean favorite
){

    public static IndexInfoCreateCommand of(IndexInfoCreateRequest request) {
        return IndexInfoCreateCommand.builder()
            .indexClassification(request.indexClassification())
            .indexName(request.indexName())
            .employedItemsCount(request.employedItemsCount())
            .basePointInTime(TimeUtils.formatedTimeInstant(request.basePointInTime()))
            .baseIndex(request.baseIndex())
            .favorite(request.favorite())
            .build();
    }

    public static IndexInfoCreateCommand fromApi(IndexInfoCreateRequest request) {
        return IndexInfoCreateCommand.builder()
            .indexClassification(request.indexClassification())
            .indexName(request.indexName())
            .employedItemsCount(request.employedItemsCount())
            .basePointInTime(TimeUtils.formatedTimeInstantFromApi(request.basePointInTime()))
            .baseIndex(request.baseIndex())
            .favorite(request.favorite())
            .build();
    }
}
