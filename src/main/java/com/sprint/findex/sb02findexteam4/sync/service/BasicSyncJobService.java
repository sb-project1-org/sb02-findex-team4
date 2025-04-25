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
      log.info("마지막 자동 연동 설정부터 지금까지의 기록을 가져옵니다.");
      String lastDate = TimeUtils.formatedTimeString(syncJobHistory.getJobTime());
      syncIndexInfoFromApi();
      syncIndexDataFromApi(TimeUtils.normalizeToDashedDate(lastDate),
          TimeUtils.normalizeToDashedDate(now));
    } catch (NotFoundException e) {
      log.warn("Sync Job History 를 발견할 수 없었습니다. 1달 이전 기록을 불러옵니다.");
      String oneMonthAgoDate = TimeUtils.oneMonthAgoDateToString();
      syncIndexInfoFromApi();
      syncIndexDataFromApi(oneMonthAgoDate, TimeUtils.normalizeToDashedDate(now));
    }
  }

  @Override
  @Transactional
  public List<SyncJobHistoryDto> syncIndexInfoFromApi() {
    List<AutoSyncConfig> enableList = autoSyncConfigRepository.findAllByEnabledIsTrue();
    log.info("enabled 성공적으로 불러옴");
    List<IndexInfoCreateRequest> apiResponse = scheduledTasks.fetchIndexInfo();
    log.info("api 성공적으로 불러옴");
    List<SyncJobHistoryDto> result = new ArrayList<>();
    for (IndexInfoCreateRequest request : apiResponse) {
      boolean isUpdate = false;
      for (int i = 0; i < enableList.size(); i++) {
        AutoSyncConfig autoSyncConfig = enableList.get(i);
        if (!isUpdate && request.indexName().equals(autoSyncConfig.getIndexInfo().getIndexName())
            && request.indexClassification()
            .equals(autoSyncConfig.getIndexInfo().getIndexClassification())) {

          log.info("{} 값이 같음 {} ", request.indexName(),
              autoSyncConfig.getIndexInfo().getIndexName());

          IndexInfo indexInfo = autoSyncConfig.getIndexInfo();
          indexInfo.updateFromDto(IndexInfoCreateCommand.fromApi(request));
          log.info("{} 성공적으로 업데이트함", i);
          isUpdate = true;

          SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
              SyncJobHistoryCreateDto.forIndexInfo(indexInfo, "system"));

          result.add(new SyncJobHistoryDto(syncJobHistory.getId(), syncJobHistory.getJobType(),
              syncJobHistory.getIndexInfo().getId(), syncJobHistory.getTargetDate(),
              syncJobHistory.getWorker(), syncJobHistory.getJobTime(),
              syncJobHistory.getJobResult()));
          log.info("성공적으로 리스트에 추가함");
        }
      }
      if (!indexInfoRepository.existsByIndexName(((request.indexName())))) {

        log.info("값이 틀림 ");

        IndexInfo indexInfo = indexInfoService.registerIndexInfoFromApi(
            IndexInfoCreateCommand.fromApi(request));

        log.info("성공적으로 생성함");

        SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
            SyncJobHistoryCreateDto.forIndexInfo(indexInfo, "system"));

        result.add(new SyncJobHistoryDto(syncJobHistory.getId(), syncJobHistory.getJobType(),
            syncJobHistory.getIndexInfo().getId(), syncJobHistory.getTargetDate(),
            syncJobHistory.getWorker(), syncJobHistory.getJobTime(),
            syncJobHistory.getJobResult()));
        log.info("성공적으로 리스트에 추가함");
      }
    }
    return result;
  }

  @Override
  @Transactional
  public List<SyncJobHistoryDto> syncIndexDataFromApi(String baseDateFrom, String baseDateTo) {
    log.info("시작일자 : {} , 종료일자: {}", baseDateFrom, baseDateTo);
    List<AutoSyncConfig> enableList = autoSyncConfigRepository.findAllByEnabledIsTrue();
    log.info("enabled 성공적으로 불러옴");
    List<IndexDataFromApi> apiResponse = scheduledTasks.fetchIndexData(
        TimeUtils.formatedStringFromDashedDate(baseDateFrom),
        TimeUtils.formatedStringFromDashedDate(baseDateTo));
    log.info("api 성공적으로 불러옴");
    List<SyncJobHistoryDto> result = new ArrayList<>();
    for (IndexDataFromApi indexDataFromApi : apiResponse) {
      for (AutoSyncConfig autoSyncConfig : enableList) {
        if (!indexDataRepository.existsByIndexInfoIdAndBaseDate(
            autoSyncConfig.getIndexInfo().getId(),
            TimeUtils.formatedTimeInstantFromApi(indexDataFromApi.baseDate()))) {
          
          indexDataService.create(
              IndexDataCreateRequest.from(autoSyncConfig.getIndexInfo().getId(), indexDataFromApi),
              SourceType.OPEN_API);

          log.info("성공적으로 생성함");
          SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
              SyncJobHistoryCreateDto.forIndexInfo(autoSyncConfig.getIndexInfo(), "system"));

          result.add(new SyncJobHistoryDto(syncJobHistory.getId(), syncJobHistory.getJobType(),
              syncJobHistory.getIndexInfo().getId(), syncJobHistory.getTargetDate(),
              syncJobHistory.getWorker(), syncJobHistory.getJobTime(),
              syncJobHistory.getJobResult()));
          log.info("성공적으로 리스트에 추가함");
        } else {
          log.warn("해당 데이터는 이미 존재함");
        }
      }
    }

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
