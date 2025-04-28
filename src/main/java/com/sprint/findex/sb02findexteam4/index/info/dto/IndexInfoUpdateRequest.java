package com.sprint.findex.sb02findexteam4.index.info.dto;

import java.time.Instant;

public record IndexInfoUpdateRequest(
    Integer employedItemsCount,
    String basePointInTime,
    Double baseIndex,
    Boolean favorite
) {
}
