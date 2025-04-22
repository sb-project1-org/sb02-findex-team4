package com.sprint.findex.sb02findexteam4.exception;

public class ExternalApiException extends SystemException {

  public ExternalApiException(ErrorCode errorCode) {
    super(errorCode);
  }
}
