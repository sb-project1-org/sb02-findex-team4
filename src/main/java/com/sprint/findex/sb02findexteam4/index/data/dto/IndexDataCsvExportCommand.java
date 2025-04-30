package com.sprint.findex.sb02findexteam4.index.data.dto;

import java.time.LocalDate;

public record IndexDataCsvExportCommand(
    Long indexInfoId,
    LocalDate startDate,
    LocalDate endDate,
    String sortField,
    String sortDirection
) {

}
