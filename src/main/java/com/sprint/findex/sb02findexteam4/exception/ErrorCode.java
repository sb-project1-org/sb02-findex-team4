package com.sprint.findex.sb02findexteam4.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  // 404
  INDEX_INFO_NOT_FOUND(404, "INDEX_INFO_NOT_FOUND", "error.index.info.not.found"),
  INDEX_DATA_NOT_FOUND(404, "INDEX_INFO_NOT_FOUND", "error.index.info.not.found"),
  AUTO_SYNC_NOT_FOUND(404, "AUTO_SYNC_NOT_FOUND", "error.auto.sync.not.found"),

  // 400



  // 500
  INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "error.internal.server.error");

  private final int httpStatus;
  private final String message;
  private final String detail;
}
