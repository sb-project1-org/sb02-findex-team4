package com.sprint.findex.sb02findexteam4.exception;

public class NotFoundException extends SystemException {

  public NotFoundException(ErrorCode errorCode, Object... args) {
    super(errorCode, args);
  }
}
