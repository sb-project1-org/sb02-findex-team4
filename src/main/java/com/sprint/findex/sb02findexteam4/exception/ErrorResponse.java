package com.sprint.findex.sb02findexteam4.exception;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String message,
        String details
) {
}
