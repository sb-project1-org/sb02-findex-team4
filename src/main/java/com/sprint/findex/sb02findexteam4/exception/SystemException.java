package com.sprint.findex.sb02findexteam4.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SystemException extends RuntimeException {
    private ErrorCode errorCode;
}
