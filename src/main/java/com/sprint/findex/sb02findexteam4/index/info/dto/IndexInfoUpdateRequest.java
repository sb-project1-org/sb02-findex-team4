package com.sprint.findex.sb02findexteam4.index.info.dto;

import java.time.LocalDate;

public record IndexInfoUpdateRequest(
    Integer employedItemsCount,
    LocalDate basePointInTime,
    Double baseIndex,
    Boolean favorite
) {

}
