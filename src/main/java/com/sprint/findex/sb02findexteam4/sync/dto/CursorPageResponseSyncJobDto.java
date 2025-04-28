package com.sprint.findex.sb02findexteam4.sync.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "커서 기반 연동 이력 페이지 응답 DTO")
public record CursorPageResponseSyncJobDto(
    @Schema(
        description = "페이지 내용 (SyncJobHistory 목록)",
        implementation = SyncJobHistoryDto.class
    )
    List<SyncJobHistoryDto> content,
    @Schema(
        description = "다음 페이지 커서 (날짜 스트링 기반)",
        example = "2025-04-20"
    )
    String nextCursor,
    @Schema(
        description = "다음 요소의 ID",
        example = "1"
    )
    Long nextIdAfter,
    @Schema(
        description = "페이지 크기",
        example = "10"
    )
    int size,
    @Schema(
        description = "총 요소 수",
        example = "100"
    )
    Long totalElements,
    @Schema(
        description = "다음 페이지 존재 여부",
        example = "true"
    )
    boolean hasNext
) {

}
