package com.sprint.findex.sb02findexteam4.sync.controller;

import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseSyncJobDto;
import com.sprint.findex.sb02findexteam4.sync.dto.IndexDataSyncRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryCreateDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobSearchCondition;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobSearchRequest;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.sync.service.SyncJobHistoryService;
import com.sprint.findex.sb02findexteam4.sync.service.SyncJobService;
import com.sprint.findex.sb02findexteam4.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sync-jobs")
public class SyncJobController {

    public final SyncJobService syncJobService;
    public final SyncJobHistoryService syncJobHistoryService;

    @PostMapping("/index-infos")
    public ResponseEntity<List<SyncJobHistoryDto>> syncIndexInfos(HttpServletRequest request) {
        String ip = IpUtils.getIp(request);
        List<SyncJobHistoryDto> syncJobHistoryDtos = syncJobService.syncIndexInfo(ip);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(syncJobHistoryDtos);
    }

    @PostMapping("/index-data")
    public ResponseEntity<List<SyncJobHistoryDto>> syncIndexData(@RequestBody IndexDataSyncRequest indexDataSyncRequest, HttpServletRequest request) {
        String ip = IpUtils.getIp(request);
        List<SyncJobHistoryDto> syncJobHistoryDtos = syncJobService.syncIndexData(indexDataSyncRequest, ip);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(syncJobHistoryDtos);
    }

    @GetMapping
    public ResponseEntity<CursorPageResponseSyncJobDto> getSyncJobHistory(
        @ModelAttribute SyncJobSearchRequest request,
        @RequestParam(defaultValue = "jobTime", required = false) String sortField,
        @RequestParam(defaultValue = "desc", required = false) String sortDirection,
        @RequestParam(defaultValue = "10", required = false) int size
    ){
        SyncJobSearchCondition condition = SyncJobSearchCondition.of(request, sortField, sortDirection, size);
        log.info(condition.toString());
        CursorPageResponseSyncJobDto indexInfoSyncJobs = syncJobHistoryService.findHistoryByCursor(condition);

        return ResponseEntity.status(HttpStatus.OK).body(indexInfoSyncJobs);
    }

    @PostMapping("/test/history")
    public ResponseEntity<SyncJobHistory> testCreateHistory(@RequestBody SyncJobHistoryCreateDto syncJobHistoryCreateDto){
        SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(syncJobHistoryCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(syncJobHistory);
    }
}
