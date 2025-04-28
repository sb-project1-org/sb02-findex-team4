package com.sprint.findex.sb02findexteam4.index.info.dto;

import com.sprint.findex.sb02findexteam4.sync.dto.api.Item;
import lombok.Builder;

@Builder
public record IndexInfoCreateRequest(
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    String basePointInTime,
    Double baseIndex,
    Boolean favorite
) {

  public static IndexInfoCreateRequest of(Item item) {
    return IndexInfoCreateRequest.builder()
        .indexClassification(item.idxCsf())
        .indexName(item.idxNm())
        .employedItemsCount(item.epyItmsCnt())
        .basePointInTime(item.basDt())
        .baseIndex(Double.valueOf(item.basIdx()))
        .favorite(false)
        .build();
  }
}
