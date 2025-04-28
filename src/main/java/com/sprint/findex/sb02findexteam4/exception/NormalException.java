package com.sprint.findex.sb02findexteam4.exception;

import lombok.Getter;

@Getter
public class NormalException extends RuntimeException {

  private ErrorCode errorCode;
  private Object[] args;

  public NormalException(ErrorCode errorCode, Object... args) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.args = args;
  }
}