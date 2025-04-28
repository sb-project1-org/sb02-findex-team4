package com.sprint.findex.sb02findexteam4.index.data.dto;

public record IndexDataFindCommand(
    Long indexInfoId,
    String startDate,
    String endDate,
    Long idAfter,
    String cursor,
    String sortField,
    String sortDirection,
    int size
) {
}

