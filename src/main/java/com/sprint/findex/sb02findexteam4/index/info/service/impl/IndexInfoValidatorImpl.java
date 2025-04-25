package com.sprint.findex.sb02findexteam4.index.info.service.impl;

import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_INFO_BAD_REQUEST;

import com.sprint.findex.sb02findexteam4.exception.NormalException;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.service.IndexInfoValidator;
import org.springframework.stereotype.Component;

@Component
public class IndexInfoValidatorImpl implements IndexInfoValidator {

  @Override
  public void validateForCreate(IndexInfoCreateRequest dto) {
    if (dto.indexClassification() == null || dto.indexClassification().isBlank()) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }

    if (dto.indexName() == null || dto.indexName().isBlank()) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }

    if (dto.employedItemsCount() == null || dto.employedItemsCount() < 0) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }

    if (dto.basePointInTime() == null) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }

    if (dto.baseIndex() == null || dto.baseIndex() < 0) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }

    if (dto.favorite() == null) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }
  }

  @Override
  public void validateForCreate(IndexInfoCreateCommand dto) {
    if (dto.indexClassification() == null || dto.indexClassification().isBlank()) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }

    if (dto.indexName() == null || dto.indexName().isBlank()) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }

    if (dto.employedItemsCount() == null || dto.employedItemsCount() < 0) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }

    if (dto.basePointInTime() == null) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }

    if (dto.baseIndex() == null || dto.baseIndex() < 0) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }

    if (dto.favorite() == null) {
      throw new NormalException(INDEX_INFO_BAD_REQUEST);
    }
  }
}
