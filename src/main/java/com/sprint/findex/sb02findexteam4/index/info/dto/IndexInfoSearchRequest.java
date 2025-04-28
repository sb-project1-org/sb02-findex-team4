package com.sprint.findex.sb02findexteam4.index.info.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexInfoSearchRequest {

  String indexClassification;
  String indexName;
  boolean favorite;
  Long idAfter;
  String cursor;
}
