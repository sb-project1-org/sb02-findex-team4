package com.sprint.findex.sb02findexteam4.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  // 404
  INDEX_INFO_NOT_FOUND(404, "error.message.not-found", "error.details.indexInfo.not-found"),
  INDEX_DATA_NOT_FOUND(404, "error.message.not-found", "error.details.indexData.not-found"),
  AUTO_SYNC_NOT_FOUND(404, "error.message.not-found", "error.details.autoSyncConfig.not-found"),
  SYNC_JOB_HISTORY_NOT_FOUND(404,"error.message.not-found","error.details.syncJobHistory.not-found"),

  // 400 잘못된 요청
  INDEX_INFO_BAD_REQUEST(400, "error.message.badRequest", "error.details.indexInfo.badRequest"),
  INDEX_DATA_BAD_REQUEST(400, "error.message.badRequest", "error.details.indexData.badRequest"),
  AUTO_SYNC_BAD_REQUEST(400, "error.message.badRequest", "error.details.syncJobHistory.badRequest"),
  SYNC_JOB_HISTORY_BAD_REQUEST(400,"error.message.badRequest","error.details.autoSyncConfig.badRequest"),
  EXTERNAL_API_BAD_REQUEST(400,"error.message.badRequest","error.details.external-api.badRequest"),

  // 400 이미 존재함
  INDEX_INFO_ALREADY_EXISTS(400, "error.message.already-exists", "error.details.indexInfo.already-exists"),
  INDEX_DATA_ALREADY_EXISTS(400, "error.message.already-exists", "error.details.indexData.already-exists"),
  AUTO_SYNC_ALREADY_EXISTS(400, "error.message.already-exists", "error.details.syncJobHistory.already-exists"),
  SYNC_JOB_HISTORY_ALREADY_EXISTS(400, "error.message.already-exists", "error.details.autoSyncConfig.already-exists"),

  // 500
  INTERNAL_SERVER_ERROR(500, "error.message.internal", "error.details.internal"),
  EXTERNAL_API_INERNAL(504,"error.message.internal","error.details.external-api.internal"),
  EXTERNAL_API_BAD_GATE_WAY(502,"error.message.internal","error.details.external-api.BadGateWay"),
  EXTERNAL_API_TIMEOUT(504,"error.message.internal","error.details.external-api.GateTimeOut");



  private final int httpStatus;
  private final String message;
  private final String detail;
}
