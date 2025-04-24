package com.sprint.findex.sb02findexteam4.index.info.dto;

import java.time.ZonedDateTime;

public record IndexInfoUpdateRequest(
    Integer employedItemsCount,
    ZonedDateTime basePointInTime,
    Double baseIndex,
    Boolean favorite
) {
}
