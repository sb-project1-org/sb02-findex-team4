package com.sprint.findex.sb02findexteam4.sync.dto;

import java.time.LocalDate;
import java.util.List;

public record IndexDataSyncCommand(
    List<Long> indexInfoIds,
    LocalDate baseDateFrom,
    LocalDate baseDateTo
) {

  public static IndexDataSyncCommand toCommand(IndexDataSyncRequest request) {
    return new IndexDataSyncCommand(
        request.indexInfoIds(),
        request.baseDateFrom(),
        request.baseDateTo()
    );
  }
}
