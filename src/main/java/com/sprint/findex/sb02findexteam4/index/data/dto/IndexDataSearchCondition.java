package com.sprint.findex.sb02findexteam4.index.data.dto;

import java.time.LocalDate;

public record IndexDataSearchCondition(
    Long indexInfoId,
    LocalDate startDate,
    LocalDate endDate,
    Long idAfter,
    String cursor,
    String sortField,
    String sortDirection,
    Integer size
) {

  public static final int DEFAULT_SIZE = 10;

  public IndexDataSearchCondition {
    if (size == null || size < 1) {
      size = DEFAULT_SIZE;
    }

    if (sortDirection == null || sortDirection.isBlank()) {
      sortDirection = "desc";
    }

    if (sortField == null || sortField.isBlank()) {
      sortField = "baseDate";
    }
  }
}

