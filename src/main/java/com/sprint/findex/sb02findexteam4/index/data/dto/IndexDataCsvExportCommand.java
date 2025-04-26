package com.sprint.findex.sb02findexteam4.index.data.dto;

import lombok.Builder;

@Builder
public record IndexDataCsvExportCommand(
    Long indexInfoId,
    String startDate,
    String endDate,
    String sortField,
    String sortDirection
) {}
