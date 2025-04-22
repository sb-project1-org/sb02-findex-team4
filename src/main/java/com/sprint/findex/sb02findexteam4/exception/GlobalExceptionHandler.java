package com.sprint.findex.sb02findexteam4.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response = ErrorResponse.of(errorCode);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException e) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response = ErrorResponse.of(errorCode);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException e) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response = ErrorResponse.of(errorCode);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(ExternalApiException.class)
  public ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException e) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response = ErrorResponse.of(errorCode);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(NormalException.class)
  public ResponseEntity<ErrorResponse> handleNormalException(NormalException e) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response = ErrorResponse.of(errorCode);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(SystemException.class)
  public ResponseEntity<ErrorResponse> handleSystemException(SystemException e) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response = ErrorResponse.of(errorCode);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }
}
