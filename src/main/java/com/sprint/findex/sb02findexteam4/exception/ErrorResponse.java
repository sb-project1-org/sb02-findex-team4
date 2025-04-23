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

  public static ErrorResponse of(int httpStatus, String detail, String message) {
    return ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(httpStatus)
        .message(detail)
        .details(message).build();
  }
}
