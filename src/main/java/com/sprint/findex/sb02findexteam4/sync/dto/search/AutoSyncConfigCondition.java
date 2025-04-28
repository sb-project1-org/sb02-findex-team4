package com.sprint.findex.sb02findexteam4.sync.dto.search;

import lombok.Builder;

@Builder
public record AutoSyncConfigCondition(
    Long indexInfoId,
    Boolean enabled,
    Long idAfter,
    String cursor,
    String sortField,
    String sortDirection,
    int size
) {

  public static AutoSyncConfigCondition from(AutoSyncConfigSearchRequest request, Boolean enabled,
      String sortField, String sortDirection, int size) {
    return AutoSyncConfigCondition.builder()
        .indexInfoId(request.indexInfoId)
        .enabled(enabled)
        .idAfter(request.idAfter)
        .cursor(request.cursor)
        .sortField(sortField)
        .sortDirection(sortDirection)
        .size(size)
        .build();
  }
}
