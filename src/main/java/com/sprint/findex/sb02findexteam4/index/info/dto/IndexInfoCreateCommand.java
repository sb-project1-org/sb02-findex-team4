package com.sprint.findex.sb02findexteam4.index.info.dto;

import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.time.Instant;
import lombok.Builder;

@Builder
public record IndexInfoCreateCommand(
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    Instant basePointInTime,
    Double baseIndex,
    Boolean favorite,
    SourceType sourceType
) {

  public static IndexInfoCreateCommand fromUser(IndexInfoCreateRequest request) {
    return IndexInfoCreateCommand.builder()
        .indexClassification(request.indexClassification())
        .indexName(request.indexName())
        .employedItemsCount(request.employedItemsCount())
        .basePointInTime(TimeUtils.formatedTimeInstant(request.basePointInTime()))
        .baseIndex(request.baseIndex())
        .favorite(request.favorite())
        .sourceType(SourceType.USER)
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
        .sourceType(SourceType.OPEN_API)
        .build();
  }
}
