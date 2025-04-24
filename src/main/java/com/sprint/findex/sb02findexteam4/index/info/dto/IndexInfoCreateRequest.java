package com.sprint.findex.sb02findexteam4.index.info.dto;

public record IndexInfoCreateRequest(
   String indexClassification,
   String indexName,
   Integer employedItemsCount,
   String basePointInTime,
   Double baseIndex,
   Boolean favorite
) {
}
