package com.sprint.findex.sb02findexteam4.sync.controller;

import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryCreateDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.sync.service.SyncJobHistoryService;
import com.sprint.findex.sb02findexteam4.sync.service.SyncJobService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sync-jobs")
public class SyncJobController {

  public final SyncJobService syncJobService;
  public final SyncJobHistoryService syncJobHistoryService;

  @PostMapping("/index-infos")
  public ResponseEntity<List<SyncJobHistoryDto>> syncIndexInfos(){
    List<SyncJobHistoryDto> indexInfoSyncJobs = syncJobService.syncIndexInfos();

    return ResponseEntity.status(202).body(indexInfoSyncJobs);
  }

  @PostMapping("/index-infos/history")
  public ResponseEntity<SyncJobHistory> syncIndexInfosHistory(@RequestBody SyncJobHistoryCreateDto syncJobHistoryCreateDto){
    SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(syncJobHistoryCreateDto);

    return ResponseEntity.status(201).body(syncJobHistory);
  }
}
