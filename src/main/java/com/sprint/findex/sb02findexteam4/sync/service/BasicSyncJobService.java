package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.NotFoundException;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateCommand;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import com.sprint.findex.sb02findexteam4.index.data.repository.IndexDataRepository;
import com.sprint.findex.sb02findexteam4.index.data.service.IndexDataService;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.index.info.repository.IndexInfoRepository;
import com.sprint.findex.sb02findexteam4.index.info.service.IndexInfoService;
import com.sprint.findex.sb02findexteam4.sync.dto.IndexDataSyncCommand;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryCreateDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import com.sprint.findex.sb02findexteam4.sync.dto.api.IndexDataFromApi;
import com.sprint.findex.sb02findexteam4.sync.entity.AutoSyncConfig;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.sync.mapper.SyncJobHistoryMapper;
import com.sprint.findex.sb02findexteam4.sync.repository.AutoSyncConfigRepository;
import com.sprint.findex.sb02findexteam4.sync.repository.SyncJobHistoryRepository;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  /**
   * <H2> 자동 연동 시스템</H2>
   * 1일 주기로 연동을 시작합니다. <br> 활성화된 지수 목록만 가져옵니다. <br>
   */
  @Override
  @Transactional
//  @Scheduled(cron = "0 * * * * *")
  public void syncAll() {
    Instant now = Instant.now();
    try {
      //마지막 작업 연동을 가져오기 위해 syncJobHistory 가져옴
      SyncJobHistory syncJobHistory = syncJobHistoryRepository.findTopByOrderByJobTimeDesc()
          .orElseThrow(
              () -> new NotFoundException(ErrorCode.SYNC_JOB_HISTORY_NOT_FOUND)
          );
      log.info("[Basic Sync Job Service] sync All - load Sync Job History ");
      //yyyy-MM-dd 시간대 필요
      //서버 시간대가 필요함
      //시차 때문에 달라질 수 있다.
      //마지막 작업 연동 일자가 필요하기 때문에 => 제약조건

      //지수 정보 연동
      syncIndexInfoFromApi();
      log.info("[Basic Sync Job Service] sync All - call syncIndexInfoFromApi");

      //지수 데이터 연동
      //yyyy-MM-dd가 yyyy-MM-dd로 필요함
      //1번 방안, 메서드 안에서 바꾸는 로직을 두자
      syncIndexDataFromApi(syncJobHistory.getJobTime(), now);

      log.info("[Basic Sync Job Service] sync All - call syncIndexDataFromApi");
    } catch (NotFoundException e) {
      //기록이 없을 때, 한달 전 날짜를 가져옴
      //String 타입: yyyy-MM-dd
      Instant oneMonthAgoDate = TimeUtils.oneMonthAgoDateToString();

      syncIndexInfoFromApi();
      log.info(
          "[Basic Sync Job Service] empty DB, sync All - call syncIndexInfoFromApi");
      //지수 데이터 연동
      //yyyy-MM-dd가 yyyyMMdd로 필요함
      //1번 방안, 메서드 안에서 바꾸는 로직을 두자
      syncIndexDataFromApi(oneMonthAgoDate, now);

      log.info(
          "[Basic Sync Job Service] empty DB, sync All - call syncIndexDataFromApi");
    }
  }

  @Override
  @Transactional
  public List<SyncJobHistoryDto> syncIndexInfoFromApi() {
    //enabled List 호출
    List<AutoSyncConfig> enableList = autoSyncConfigRepository.findAllByEnabledIsTrue();
    log.info("[Basic Sync Job Service] syncIndexInfoFromApi - load enabled List, size {}",
        enableList.size());

    //api Response 호출
    List<IndexInfoCreateRequest> apiResponse = scheduledTasks.fetchIndexInfo();
    log.info("[Basic Sync Job Service] syncIndexInfoFromApi - load api Response, size {}",
        enableList.size());

    List<SyncJobHistoryDto> result = new ArrayList<>();

    //api 응답에서 create request 가져옴
    for (IndexInfoCreateRequest request : apiResponse) {
      //업데이트 했는지 확인할 목적
      boolean isUpdate = false;
      for (int i = 0; i < enableList.size(); i++) {
        //자동 연동 설정에서 데이터를 가져옴
        AutoSyncConfig autoSyncConfig = enableList.get(i);

        if (!isUpdate && request.indexName().equals(autoSyncConfig.getIndexInfo().getIndexName())
            && request.indexClassification()
            .equals(autoSyncConfig.getIndexInfo().getIndexClassification())) {

          IndexInfo indexInfo = autoSyncConfig.getIndexInfo();
          indexInfo.updateFromDto(IndexInfoCreateCommand.fromApi(request));
//          log.info("Index Info 성공적으로 업데이트됨");

          SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
              SyncJobHistoryCreateDto.forIndexInfo(indexInfo, "system"));

          result.add(SyncJobHistoryDto.fromIndexInfo(syncJobHistory));

          isUpdate = true;
        }
      }
      if (!indexInfoRepository.existsByIndexName(((request.indexName())))) {
        IndexInfo indexInfo = indexInfoService.registerIndexInfoFromApi(
            IndexInfoCreateCommand.fromApi(request));
//        log.info("Index Info 성공적으로 생성됨");

        SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
            SyncJobHistoryCreateDto.forIndexInfo(indexInfo, "system"));

        result.add(SyncJobHistoryDto.fromIndexInfo(syncJobHistory));
      }
    }

//    log.info("지수 정보 동기화 작업 완료 : 대상 수 : {}", result.size());
    return result;
  }

  /**
   * <h2>수동 Index Data 연동</h2>
   * 사용자가 직접 Index Data를 연동한다. 날짜와 정보는
   *
   * @param baseDateFrom Insant타입
   * @param baseDateTo   Insant 타입
   * @return
   */
  @Override
  @Transactional
  public List<SyncJobHistoryDto> syncIndexDataFromApi(Instant baseDateFrom, Instant baseDateTo) {
    //연동 기록 목록을 저장해둘 result
    List<SyncJobHistory> result = new ArrayList<>();

    log.info("[Basic Sync Job Service] syncIndexDataFromApi - baseDateFrom : {} , baseDateTo: {}",
        baseDateFrom, baseDateTo);
    List<AutoSyncConfig> enableList = autoSyncConfigRepository.findAllByEnabledIsTrue();
    log.info("[Basic Sync Job Service] syncIndexDataFromApi - load enabled List, size : {}",
        enableList.size());
    List<IndexInfo> indexInfoList = enableList.stream().map(AutoSyncConfig::getIndexInfo).toList();

    for (IndexInfo indexInfo : indexInfoList) {
      List<IndexDataFromApi> apiResponse = scheduledTasks.fetchIndexData(indexInfo.getIndexName(),
          baseDateFrom, baseDateTo);
      for (IndexDataFromApi indexDataFromApi : apiResponse) {
        SyncJobHistory syncJobHistory;
        Optional<IndexData> optionalIndexData = indexDataRepository.findByIndexInfoIdAndBaseDate(
            indexInfo.getId(), indexDataFromApi.baseDate());
        if (optionalIndexData.isPresent()) {
          IndexData indexData = optionalIndexData.get();
          indexData.update(IndexDataUpdateRequest.fromApi(indexDataFromApi));
          log.info("[Basic Sync Job Service] syncIndexDataFromApi - update Index Data : {}",
              indexData.getId());
        } else {
          IndexDataResponse response = indexDataService.create(
              IndexDataCreateCommand.fromApi(indexInfo.getId(), indexDataFromApi,
                  SourceType.OPEN_API));
          log.info("[Basic Sync Job Service] syncIndexDataFromApi - create Index Data : {}",
              response.id());
        }
        syncJobHistory = syncJobHistoryService.saveHistory(
            SyncJobHistoryCreateDto.forIndexData(
                indexInfo,
                "system",
                indexDataFromApi.baseDate()
            )
        );
        result.add(syncJobHistory);
      }
    }
    return result.stream().map(SyncJobHistoryDto::fromIndexData).toList();
  }

  @Transactional
  @Override
  public List<SyncJobHistoryDto> syncIndexInfo(String ip) {
    // 1. 현재 DB에 존재하는 index info들을 가져온다
    List<IndexInfo> indexInfoList = indexInfoRepository.findAll();
    log.info("indexInfoList 성공적으로 불러옴");

    // 2. 외부 API를 통해 가져온다
    List<IndexInfoCreateRequest> apiResponse = scheduledTasks.fetchIndexInfo();
    log.info("api 성공적으로 불러옴");

    // 3. 기존 IndexInfo를 "key" 기준으로 Map화 (indexClassification + "|" + indexName)
    Map<String, IndexInfo> existingIndexInfoMap = indexInfoList.stream()
        .collect(Collectors.toMap(
            IndexInfo -> IndexInfo.getIndexClassification() + "|" + IndexInfo.getIndexName(),
            indexInfo -> indexInfo)
        );

    // 4. 이번 트랜잭션 안에서 새로 생성(save)한 key들도 관리
    Set<String> newCreateKeys = new HashSet<>();

    List<SyncJobHistoryDto> result = new ArrayList<>();

    for (IndexInfoCreateRequest request : apiResponse) {
      String key = request.indexClassification() + "|" + request.indexName();

      // 5. 기존 DB or 이번 트랜잭션 생성 내역에 있는지 확인
      if (existingIndexInfoMap.containsKey(key)) { // update
        log.info("값이 같음 {} ", key);
        IndexInfo existing = existingIndexInfoMap.get(key);
        existing.updateFromDto(IndexInfoCreateCommand.fromApi(request));

        SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
            SyncJobHistoryCreateDto.forIndexInfo(existing, ip));

        result.add(SyncJobHistoryMapper.toDto(syncJobHistory));
      } else if (!newCreateKeys.contains(key)) { // create
        log.info("값이 없음 {}", key);

        IndexInfo indexInfo = indexInfoService.registerIndexInfoFromApi(
            IndexInfoCreateCommand.fromApi(request));

        SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
            SyncJobHistoryCreateDto.forIndexInfo(indexInfo, ip));

        newCreateKeys.add(key);
        result.add(SyncJobHistoryMapper.toDto(syncJobHistory));
      } else {
        log.warn("이미 생성 대기 중인 키 중복 감지: {}", key);
      }
    }

    return result;
  }

  @Transactional
  @Override
  public List<SyncJobHistoryDto> syncIndexData(IndexDataSyncCommand request, String ip) {
    List<Long> indexInfoIds = request.indexInfoIds();
    List<IndexInfo> indexInfoList = new ArrayList<>();
    List<SyncJobHistoryDto> result = new ArrayList<>();

    //지수 아이디 목록에서 하나씩 꺼낸다.
    for (Long indexInfoId : indexInfoIds) {
      //지수 정보를 지수 아이디를 통해서 레포지토리에서 하나씩 꺼낸다.
      Optional<IndexInfo> optionalIndexInfo = indexInfoRepository.findById(indexInfoId);
      //만약 지수 정보가 있다면 지수 정보를 꺼내서 리스트에 저장한다.
      optionalIndexInfo.ifPresent(indexInfoList::add);
    }

    log.info("생성된 지수 목록의 수 : {}", indexInfoList.size());
    for (IndexInfo indexInfo : indexInfoList) {
      //지수 정보의 이름, DateFrom, DateTo를 통해서 api 응답을 받는다.
      List<IndexDataFromApi> apiResponse = scheduledTasks.fetchIndexData(
          indexInfo.getIndexName(),
          request.baseDateFrom(),
          request.baseDateTo()
      );
      log.info("생성된 api 데이터 목록의 수 : {}", apiResponse.size());

      Long indexInfoId = indexInfo.getId();
      //지수 데이터를 하나씩 순회를 시작한다.
      for (IndexDataFromApi indexDataFromApi : apiResponse) {
        Instant baseDateFromApi = indexDataFromApi.baseDate();
        IndexInfo indexInfoFromApi = indexInfoRepository.findByIndexClassificationAndIndexName(
            indexDataFromApi.indexClassification(), indexDataFromApi.indexName()).orElse(null);

        if (indexInfoFromApi == null) {
          log.warn("IndexInfo를 찾을 수 없음: classification={}, name={}",
              indexDataFromApi.indexClassification(), indexDataFromApi.indexName());
          continue;
        }
        if (!indexInfoFromApi.getId().equals(indexInfoId)) {
          log.warn("IndexInfo ID 불일치: apiId={}, requestId={}", indexInfoFromApi.getId(),
              indexInfoId);
          continue;
        }
        //만약 지수 정보 아이디와 날짜가 일치하지 않는다면
        if (!indexDataRepository.existsByIndexInfoIdAndBaseDate(indexInfoId, baseDateFromApi)) {
          log.info("지수 정보, 날짜 불일치로 인한 지수 데이터 생성");
          //지수 데이터를 하나 만든다.
          IndexDataResponse dataResponse = indexDataService.create(
              IndexDataCreateCommand.fromApi(indexInfo.getId(), indexDataFromApi,
                  SourceType.OPEN_API));
          log.info("지수 데이터 생성 : {}", dataResponse.id());

          SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
              SyncJobHistoryCreateDto.forIndexData(indexInfo, ip, baseDateFromApi));
          log.info("연동 기록 생성 : {}", syncJobHistory.getId());

          result.add(SyncJobHistoryMapper.toDto(syncJobHistory));
        } else {  // 만약 지수 정보 아이디와 날짜가 일치하면
          log.info("지수 정보, 날짜 일치로 인한 지수 데이터 업데이트");
          IndexData indexData = indexDataRepository.findByIndexInfoIdAndBaseDate(
              indexInfoId,
              baseDateFromApi).orElse(null);
          if (indexData != null) {
            IndexData update = indexData.update(IndexDataUpdateRequest.fromApi(indexDataFromApi));
            log.info("지수 데이터 업데이트 : {}", update.getId());
            SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
                SyncJobHistoryCreateDto.forIndexData(indexInfo, ip, baseDateFromApi));
            log.info("연동 기록 생성 : {}", syncJobHistory.getId());
            result.add(SyncJobHistoryMapper.toDto(syncJobHistory));
          } else {
            log.error("index Data 업데이트에 오류 발생함, 널값이 들어갔음 -> index Name {}",
                indexInfo.getIndexName());
          }
        }
      }
    }
    log.info("총 생성된 연동기록 수 : {}", result.size());
    return result;
  }

}
