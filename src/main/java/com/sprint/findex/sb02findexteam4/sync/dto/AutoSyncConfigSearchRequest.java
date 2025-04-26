package com.sprint.findex.sb02findexteam4.sync.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoSyncConfigSearchRequest {

  Long indexInfoId;
  boolean enabled;
  Long idAfter;
  String cursor;
}
