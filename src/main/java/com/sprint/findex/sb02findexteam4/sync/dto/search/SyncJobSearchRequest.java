package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.sync.entity.JobResult;
import com.sprint.findex.sb02findexteam4.sync.entity.JobType;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SyncJobSearchRequest {

  private JobType jobType;
  private Long indexInfoId;
  private String baseDateFrom;
  private String baseDateTo;
  private String worker;
  private Instant jobTimeFrom;
  private Instant jobTimeTo;
  private JobResult status;
  private Long idAfter;
  private Long cursor;
}
