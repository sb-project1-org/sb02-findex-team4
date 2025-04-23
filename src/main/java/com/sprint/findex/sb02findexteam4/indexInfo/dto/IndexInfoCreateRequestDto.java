package com.sprint.findex.sb02findexteam4.indexInfo.dto;

import java.time.Instant;

public record IndexInfoCreateRequestDto(
   String indexClassificationName,
   String indexName,
   Integer employedItemsCount,
   Instant basePointInTime,
   Double baseIndex,
   Boolean favorite
) {
}
