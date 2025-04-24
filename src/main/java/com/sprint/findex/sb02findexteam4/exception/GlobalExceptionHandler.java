package com.sprint.findex.sb02findexteam4.exception;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final MessageSource messageSource;

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, Locale locale) {
    ErrorCode errorCode = e.getErrorCode();
    String details = messageSource.getMessage(errorCode.getDetail(), e.getArgs(), locale);
    String message = messageSource.getMessage(errorCode.getMessage(), e.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(errorCode.getHttpStatus(), details, message);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException e,
      Locale locale) {
    ErrorCode errorCode = e.getErrorCode();
    String details = messageSource.getMessage(errorCode.getDetail(), e.getArgs(), locale);
    String message = messageSource.getMessage(errorCode.getMessage(), e.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(errorCode.getHttpStatus(), details, message);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException e,
      Locale locale) {
    ErrorCode errorCode = e.getErrorCode();
    String details = messageSource.getMessage(errorCode.getDetail(), e.getArgs(), locale);
    String message = messageSource.getMessage(errorCode.getMessage(), e.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(errorCode.getHttpStatus(), details, message);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(ExternalApiException.class)
  public ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException e,
      Locale locale) {
    ErrorCode errorCode = e.getErrorCode();
    String details = messageSource.getMessage(errorCode.getDetail(), e.getArgs(), locale);
    String message = messageSource.getMessage(errorCode.getMessage(), e.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(errorCode.getHttpStatus(), details, message);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(NormalException.class)
  public ResponseEntity<ErrorResponse> handleNormalException(NormalException e, Locale locale) {
    ErrorCode errorCode = e.getErrorCode();
    String details = messageSource.getMessage(errorCode.getDetail(), e.getArgs(), locale);
    String message = messageSource.getMessage(errorCode.getMessage(), e.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(errorCode.getHttpStatus(), details, message);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(SystemException.class)
  public ResponseEntity<ErrorResponse> handleSystemException(SystemException e, Locale locale) {
    ErrorCode errorCode = e.getErrorCode();
    String details = messageSource.getMessage(errorCode.getDetail(), e.getArgs(), locale);
    String message = messageSource.getMessage(errorCode.getMessage(), e.getArgs(), locale);
    ErrorResponse response = ErrorResponse.of(errorCode.getHttpStatus(), details, message);
    logger.error(response.details());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(response);
  }
}
