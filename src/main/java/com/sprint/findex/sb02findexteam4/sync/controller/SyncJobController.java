package com.sprint.findex.sb02findexteam4.sync.controller;

import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseSyncJobDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryCreateDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobSearchCondition;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobSearchRequest;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.sync.service.SyncJobHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sync-jobs")
public class SyncJobController {

    public final SyncJobHistoryService syncJobHistoryService;

    @GetMapping
    public ResponseEntity<CursorPageResponseSyncJobDto> getSyncJobHistory(
        @ModelAttribute SyncJobSearchRequest request,
        @RequestParam(defaultValue = "jobTime", required = false) String sortField,
        @RequestParam(defaultValue = "desc", required = false) String sortDirection,
        @RequestParam(defaultValue = "10", required = false) int size
    ){
        System.out.println("시작");
        System.out.println(request);
        SyncJobSearchCondition condition = SyncJobSearchCondition.of(request, sortField, sortDirection, size);
        CursorPageResponseSyncJobDto indexInfoSyncJobs = syncJobHistoryService.findHistoryByCursor(condition);

        return ResponseEntity.status(HttpStatus.OK).body(indexInfoSyncJobs);
    }

    @PostMapping("/test/history")
    public ResponseEntity<SyncJobHistory> testCreateHistory(@RequestBody SyncJobHistoryCreateDto syncJobHistoryCreateDto){
        SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(syncJobHistoryCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(syncJobHistory);
    }
}
