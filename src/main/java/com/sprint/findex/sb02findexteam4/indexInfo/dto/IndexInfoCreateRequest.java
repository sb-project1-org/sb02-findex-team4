package com.sprint.findex.sb02findexteam4.indexInfo.dto;

import java.time.ZonedDateTime;

public record IndexInfoCreateRequest(
   String indexClassification,
   String indexName,
   Integer employedItemsCount,
   ZonedDateTime basePointInTime,
   Double baseIndex,
   Boolean favorite
) {
}
