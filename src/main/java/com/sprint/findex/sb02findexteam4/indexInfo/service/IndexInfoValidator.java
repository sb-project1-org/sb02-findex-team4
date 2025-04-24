package com.sprint.findex.sb02findexteam4.indexInfo.service;

import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoCreateRequest;

public interface IndexInfoValidator {
  void validateForCreate(IndexInfoCreateRequest dto);
}
