package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.NotFoundException;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.index.data.repository.IndexDataRepository;
import com.sprint.findex.sb02findexteam4.index.data.service.IndexDataService;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.index.info.repository.IndexInfoRepository;
import com.sprint.findex.sb02findexteam4.index.info.service.IndexInfoService;
import com.sprint.findex.sb02findexteam4.sync.dto.IndexDataFromApi;
import com.sprint.findex.sb02findexteam4.sync.dto.IndexDataSyncRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryCreateDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import com.sprint.findex.sb02findexteam4.sync.entity.AutoSyncConfig;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.sync.repository.AutoSyncConfigRepository;
import com.sprint.findex.sb02findexteam4.sync.repository.SyncJobHistoryRepository;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicSyncJobService implements SyncJobService {

  private final AutoSyncConfigService autoSyncConfigService;
  private final AutoSyncConfigRepository autoSyncConfigRepository;
  private final ScheduledTasks scheduledTasks;
  private final SyncJobHistoryService syncJobHistoryService;
  private final SyncJobHistoryRepository syncJobHistoryRepository;

  private final IndexInfoService indexInfoService;
  private final IndexDataService indexDataService;

  private final IndexInfoRepository indexInfoRepository;
  private final IndexDataRepository indexDataRepository;

  @Override
  @Transactional
  @Scheduled(fixedDelay = 20000)
  public void syncAll() {
    String now = TimeUtils.formatedTimeString(Instant.now());
    try {
      SyncJobHistory syncJobHistory = syncJobHistoryRepository.findTopByOrderByJobTimeDesc()
          .orElseThrow(
              () -> new NotFoundException(ErrorCode.SYNC_JOB_HISTORY_NOT_FOUND)
          );
      log.info("마지막 작업 연동 작업일로부터 현재까지의 연동 기록을 가져옵니다.");
      String lastDate = TimeUtils.formatedTimeString(syncJobHistory.getJobTime());
      syncIndexInfoFromApi();
      syncIndexDataFromApi(TimeUtils.normalizeToDashedDate(lastDate),
          TimeUtils.normalizeToDashedDate(now));
    } catch (NotFoundException e) {
      log.warn("연동 작업 기록을 찾을 수 없습니다. 1달간의 연동 기록을 가져옵니다.");
      String oneMonthAgoDate = TimeUtils.oneMonthAgoDateToString();
      syncIndexInfoFromApi();
      syncIndexDataFromApi(oneMonthAgoDate, TimeUtils.normalizeToDashedDate(now));
    }
  }

  @Override
  @Transactional
  public List<SyncJobHistoryDto> syncIndexInfoFromApi() {
    List<AutoSyncConfig> enableList = autoSyncConfigRepository.findAllByEnabledIsTrue();
    log.info("syncIndexInfo 메서드 enabled List 성공적으로 불러옴 - 대상 수 : {}", enableList.size());

    List<IndexInfoCreateRequest> apiResponse = scheduledTasks.fetchIndexInfo();
    log.info("Info api 응답 생성 완료 - 대상 수 : {}", apiResponse.size());

    List<SyncJobHistoryDto> result = new ArrayList<>();

    for (IndexInfoCreateRequest request : apiResponse) {
      boolean isUpdate = false;
      for (int i = 0; i < enableList.size(); i++) {
        AutoSyncConfig autoSyncConfig = enableList.get(i);
        if (!isUpdate && request.indexName().equals(autoSyncConfig.getIndexInfo().getIndexName())
            && request.indexClassification()
            .equals(autoSyncConfig.getIndexInfo().getIndexClassification())) {

          IndexInfo indexInfo = autoSyncConfig.getIndexInfo();
          indexInfo.updateFromDto(IndexInfoCreateCommand.fromApi(request));
          log.info("Index Info 성공적으로 업데이트됨");

          SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
              SyncJobHistoryCreateDto.forIndexInfo(indexInfo, "system"));

          result.add(SyncJobHistoryDto.fromIndexInfo(syncJobHistory));

          isUpdate = true;
        }
      }
      if (!indexInfoRepository.existsByIndexName(((request.indexName())))) {
        IndexInfo indexInfo = indexInfoService.registerIndexInfoFromApi(
            IndexInfoCreateCommand.fromApi(request));
        log.info("Index Info 성공적으로 생성됨");

        SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
            SyncJobHistoryCreateDto.forIndexInfo(indexInfo, "system"));

        result.add(SyncJobHistoryDto.fromIndexInfo(syncJobHistory));
      }
    }

    log.info("지수 정보 동기화 작업 완료 : 대상 수 : {}", result.size());
    return result;
  }

  @Override
  @Transactional
  public List<SyncJobHistoryDto> syncIndexDataFromApi(String baseDateFrom, String baseDateTo) {
    log.info("baseDateFrom : {} , baseDateTo: {}", baseDateFrom, baseDateTo);
    List<AutoSyncConfig> enableList = autoSyncConfigRepository.findAllByEnabledIsTrue();
    log.info("syncIndexData 메서드 enabled List 성공적으로 불러옴 - 대상 수 : {}", enableList.size());

    List<IndexDataFromApi> apiResponse = scheduledTasks.fetchIndexData(
        TimeUtils.formatedStringFromDashedDate(baseDateFrom),
        TimeUtils.formatedStringFromDashedDate(baseDateTo));
    log.info("Data api 응답 생성 완료 - 대상 수 : {}", apiResponse.size());

    List<SyncJobHistoryDto> result = new ArrayList<>();
    for (IndexDataFromApi indexDataFromApi : apiResponse) {
      for (AutoSyncConfig autoSyncConfig : enableList) {
        if (!indexDataRepository.existsByIndexInfoIdAndBaseDate(
            autoSyncConfig.getIndexInfo().getId(),
            TimeUtils.formatedTimeInstantFromApi(indexDataFromApi.baseDate()))) {

          indexDataService.create(
              IndexDataCreateRequest.from(autoSyncConfig.getIndexInfo().getId(), indexDataFromApi),
              SourceType.OPEN_API);

          log.info("Index Data 성공적으로 생성됨");
          //지수 데이터 연동 기록 남기기
          SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
              SyncJobHistoryCreateDto.forIndexData(autoSyncConfig.getIndexInfo(), "system",
                  TimeUtils.formatedTimeInstantFromApi(indexDataFromApi.baseDate())));

          result.add(SyncJobHistoryDto.fromIndexData(syncJobHistory));
        } else {
          log.warn("해당 Index Data 가 존재합니다.");
        }
      }
    }
    log.info("지수 데이터 동기화 작업 완료 : 대상 수 : {}", result.size());
    return result;
  }

  @Override
  public List<SyncJobHistoryDto> syncIndexInfo() {
    return List.of();
  }

  @Override
  public List<SyncJobHistoryDto> syncIndexData(IndexDataSyncRequest request) {
    return List.of();
  }
}
