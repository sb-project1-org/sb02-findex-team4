package com.sprint.findex.sb02findexteam4.index.data.dto;

import java.util.List;

public record CursorPageResponseIndexDataDto(
    List<IndexDataDto> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    long totalElements,
    boolean hasNext
) {
}

