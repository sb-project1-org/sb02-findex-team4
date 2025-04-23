package com.sprint.findex.sb02findexteam4.indexInfo.service;

import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoCreateRequestDto;

public interface IndexInfoValidator {
  void validateForCreate(IndexInfoCreateRequestDto dto);
}
