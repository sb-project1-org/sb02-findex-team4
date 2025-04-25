package com.sprint.findex.sb02findexteam4.index.info.service;

import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;

public interface IndexInfoValidator {
  void validateForCreate(IndexInfoCreateRequest dto);
  void validateForCreate(IndexInfoCreateCommand dto);
}
