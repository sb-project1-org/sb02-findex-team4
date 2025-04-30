package com.sprint.findex.sb02findexteam4.index.info.dto;

import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record IndexInfoCreateCommand(
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    LocalDate basePointInTime,
    Double baseIndex,
    Boolean favorite,
    SourceType sourceType
) {

  public static IndexInfoCreateCommand fromUser(IndexInfoCreateRequest request) {
    return IndexInfoCreateCommand.builder()
        .indexClassification(request.indexClassification())
        .indexName(request.indexName())
        .employedItemsCount(request.employedItemsCount())
        .basePointInTime(request.basePointInTime())
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
        .basePointInTime(request.basePointInTime())
        .baseIndex(request.baseIndex())
        .favorite(request.favorite())
        .sourceType(SourceType.OPEN_API)
        .build();
  }
}
