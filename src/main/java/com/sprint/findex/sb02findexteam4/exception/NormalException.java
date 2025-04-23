package com.sprint.findex.sb02findexteam4.exception;

import lombok.Getter;

@Getter
public class NormalException extends RuntimeException {

  private ErrorCode errorCode;

  public NormalException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
