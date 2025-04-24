package com.sprint.findex.sb02findexteam4.indexInfo.service.impl;

import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.indexInfo.service.IndexInfoValidator;
import org.springframework.stereotype.Component;

@Component
public class IndexInfoValidatorImpl implements IndexInfoValidator {

  @Override
  public void validateForCreate(IndexInfoCreateRequest dto) {
    if (dto.indexClassification() == null || dto.indexClassification().isBlank()) {
      throw new IllegalArgumentException("지표 분류는 필수입니다.");
    }

    if (dto.indexName() == null || dto.indexName().isBlank()) {
      throw new IllegalArgumentException("지표 이름은 필수입니다.");
    }

    if (dto.employedItemsCount() == null || dto.employedItemsCount() < 0) {
      throw new IllegalArgumentException("지표 항목 수는 0 이상이어야 합니다.");
    }

    if (dto.basePointInTime() == null) {
      throw new IllegalArgumentException("기준 시점은 필수입니다.");
    }

    if (dto.baseIndex() == null || dto.baseIndex() < 0) {
      throw new IllegalArgumentException("기준 지수는 0 이상이어야 합니다.");
    }

    if (dto.favorite() == null) {
      throw new IllegalArgumentException("즐겨찾기 여부는 필수입니다.");
    }
  }
}
