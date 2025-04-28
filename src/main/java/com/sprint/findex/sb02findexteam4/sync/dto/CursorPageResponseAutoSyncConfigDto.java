package com.sprint.findex.sb02findexteam4.sync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "커서 기반 페이지 응답")
public record CursorPageResponseAutoSyncConfigDto(
    @Schema(description = "페이지 내용")
    List<AutoSyncConfigDto> content,
    @Schema(description = "다음 페이지 커서")
    String nextCursor,
    @Schema(description = "마지막 요소의 ID")
    Long nextIdAfter,
    @Schema(description = "페이지 크기")
    int size,
    @Schema(description = "총 요소 수")
    Long totalElements,
    @Schema(description = "다음 페이지 여부")
    boolean hasNext
) {

}
