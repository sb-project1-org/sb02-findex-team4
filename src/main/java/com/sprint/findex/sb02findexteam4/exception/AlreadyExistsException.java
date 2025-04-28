package com.sprint.findex.sb02findexteam4.exception;

public class AlreadyExistsException extends SystemException {

  public AlreadyExistsException(ErrorCode errorCode, Object... args) {
    super(errorCode, args);
  }
}
