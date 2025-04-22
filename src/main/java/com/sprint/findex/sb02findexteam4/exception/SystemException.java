package com.sprint.findex.sb02findexteam4.exception;

import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {

  private ErrorCode errorCode;

  public SystemException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
