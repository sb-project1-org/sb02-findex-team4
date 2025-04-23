package com.sprint.findex.sb02findexteam4.exception;

import java.time.Instant;
import lombok.Builder;

@Builder
public record ErrorResponse(
        Instant timestamp,
        int status,
        String message,
        String details
) {

  public static ErrorResponse of(ErrorCode errorCode) {
    return ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(errorCode.getHttpStatus())
        .message(errorCode.getMessage())
        .details(errorCode.getDetail()).build();
  }
}
