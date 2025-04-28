package com.sprint.findex.sb02findexteam4.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Builder;

@Builder
@Schema(description = "에러 응답")
public record ErrorResponse(
    @Schema(description = "에러 발생 시각", example = "2025-04-28T09:00:00.000000Z")
    Instant timestamp,
    @Schema(description = "HTTP  상태 코드", example = "400")
    int status,
    @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
    String message,
    @Schema(description = "에러 상세 내용", example = "코드는 필수입니다.")
    String details
) {

  public static ErrorResponse of(int httpStatus, String detail, String message) {
    return ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(httpStatus)
        .message(detail)
        .details(message).build();
  }
}
