package com.sprint.findex.sb02findexteam4.sync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "지수 데이터 연동 요청 DTO")
public record IndexDataSyncRequest(
    @Schema(
        description = "지수 정보 ID 목록 (비어있을 경우 모든 지수 대상)",
        example = "[1, 2, 3]"
    )
    List<Long> indexInfoIds,
    @Schema(
        description = "대상 날짜 (부터)",
        example = "2025-04-01",
        type = "string",
        format = "date"
    )
    String baseDateFrom,
    @Schema(
        description = "대상 날짜 (까지)",
        example = "2025-04-27",
        type = "string",
        format = "date"
    )
    String baseDateTo
) {

}
