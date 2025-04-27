package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.NotFoundException;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
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
import com.sprint.findex.sb02findexteam4.sync.dto.IndexDataFromApi;
import com.sprint.findex.sb02findexteam4.sync.dto.IndexDataSyncRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryCreateDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import com.sprint.findex.sb02findexteam4.sync.entity.AutoSyncConfig;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.sync.mapper.SyncJobHistoryMapper;
import com.sprint.findex.sb02findexteam4.sync.repository.AutoSyncConfigRepository;
import com.sprint.findex.sb02findexteam4.sync.repository.SyncJobHistoryRepository;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
  @Scheduled(fixedDelay = 60000)
  public void syncAll() {
    String now = TimeUtils.formatedTimeString(Instant.now());
    try {
      SyncJobHistory syncJobHistory = syncJobHistoryRepository.findTopByOrderByJobTimeDesc()
          .orElseThrow(
              () -> new NotFoundException(ErrorCode.SYNC_JOB_HISTORY_NOT_FOUND)
          );
//      log.info("마지막 작업 연동 작업일로부터 현재까지의 연동 기록을 가져옵니다.");
      String lastDate = TimeUtils.formatedTimeString(syncJobHistory.getJobTime());
      syncIndexInfoFromApi();
      syncIndexDataFromApi(TimeUtils.normalizeToDashedDate(lastDate),
          TimeUtils.normalizeToDashedDate(now));
    } catch (NotFoundException e) {
//      log.warn("연동 작업 기록을 찾을 수 없습니다. 1달간의 연동 기록을 가져옵니다.");
      String oneMonthAgoDate = TimeUtils.oneMonthAgoDateToString();
      syncIndexInfoFromApi();
      syncIndexDataFromApi(oneMonthAgoDate, TimeUtils.normalizeToDashedDate(now));
    }
  }

  @Override
  @Transactional
  public List<SyncJobHistoryDto> syncIndexInfoFromApi() {
    List<AutoSyncConfig> enableList = autoSyncConfigRepository.findAllByEnabledIsTrue();
//    log.info("syncIndexInfo 메서드 enabled List 성공적으로 불러옴 - 대상 수 : {}", enableList.size());

    List<IndexInfoCreateRequest> apiResponse = scheduledTasks.fetchIndexInfo();
//    log.info("Info api 응답 생성 완료 - 대상 수 : {}", apiResponse.size());

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

  @Override
  @Transactional
  public List<SyncJobHistoryDto> syncIndexDataFromApi(String baseDateFrom, String baseDateTo) {
//    log.info("baseDateFrom : {} , baseDateTo: {}", baseDateFrom, baseDateTo);
    List<AutoSyncConfig> enableList = autoSyncConfigRepository.findAllByEnabledIsTrue();
//    log.info("syncIndexData 메서드 enabled List 성공적으로 불러옴 - 대상 수 : {}", enableList.size());

    List<IndexDataFromApi> apiResponse = scheduledTasks.fetchIndexData(
        TimeUtils.formatedStringFromDashedDate(baseDateFrom),
        TimeUtils.formatedStringFromDashedDate(baseDateTo));
//    log.info("Data api 응답 생성 완료 - 대상 수 : {}", apiResponse.size());

    List<SyncJobHistoryDto> result = new ArrayList<>();
    for (IndexDataFromApi indexDataFromApi : apiResponse) {
      for (AutoSyncConfig autoSyncConfig : enableList) {
        if (!indexDataRepository.existsByIndexInfoIdAndBaseDate(
            autoSyncConfig.getIndexInfo().getId(),
            TimeUtils.formatedTimeInstantFromApi(indexDataFromApi.baseDate()))) {

          indexDataService.create(
              IndexDataCreateRequest.from(autoSyncConfig.getIndexInfo().getId(), indexDataFromApi),
              SourceType.OPEN_API);

//          log.info("Index Data 성공적으로 생성됨");
          //지수 데이터 연동 기록 남기기
          SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
              SyncJobHistoryCreateDto.forIndexData(autoSyncConfig.getIndexInfo(), "system",
                  TimeUtils.formatedTimeInstantFromApi(indexDataFromApi.baseDate())));

          result.add(SyncJobHistoryDto.fromIndexData(syncJobHistory));
        } else {
//          log.warn("해당 Index Data 가 존재합니다.");
        }
      }
    }
//    log.info("지수 데이터 동기화 작업 완료 : 대상 수 : {}", result.size());
    return result;
  }

  @Transactional
  @Override
  public List<SyncJobHistoryDto> syncIndexInfo(String ip) {
    List<IndexInfo> indexInfoList = indexInfoRepository.findAll();
    log.info("indexInfoList 성공적으로 불러옴");
    List<IndexInfoCreateRequest> apiResponse = scheduledTasks.fetchIndexInfo();
    log.info("api 성공적으로 불러옴");
    List<SyncJobHistoryDto> result = new ArrayList<>();

    Map<String, IndexInfo> existingIndexInfoMap = indexInfoList.stream()
        .collect(Collectors.toMap(
            IndexInfo -> IndexInfo.getIndexClassification() + "|" + IndexInfo.getIndexName(),
            indexInfo -> indexInfo)
        );

    for (IndexInfoCreateRequest request : apiResponse) {
      String key = request.indexClassification() + "|" + request.indexName();
      if (existingIndexInfoMap.containsKey(key)) { // update
        log.info("값이 같음 {} ", key);
        IndexInfo existing = existingIndexInfoMap.get(key);
        existing.updateFromDto(IndexInfoCreateCommand.fromApi(request));

        SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
            SyncJobHistoryCreateDto.forIndexInfo(existing, ip));

        result.add(SyncJobHistoryMapper.toDto(syncJobHistory));
      } else { // create
        log.info("값이 없음 {} ", key);
        IndexInfo indexInfo = indexInfoService.registerIndexInfoFromApi(
            IndexInfoCreateCommand.fromApi(request));

        SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
            SyncJobHistoryCreateDto.forIndexInfo(indexInfo, ip));

        result.add(SyncJobHistoryMapper.toDto(syncJobHistory));
      }
    }

    return result;
  }

//  @Transactional
//  @Override
//  public List<SyncJobHistoryDto> syncIndexData(IndexDataSyncRequest request, String ip) {
//    log.info("사용자 수동 OpenAPI 연동 작업 진행 -> Ip {}, request Base Date From{} Base Date To {}", ip,
//        request.baseDateFrom(), request.baseDateTo());
//    // Request에서 받은 indexInfoId를 사용하여 해당 IndexData를 가져옵니다.
//    List<IndexData> indexDataList = indexDataRepository.findByIndexInfoIdInAndBaseDateBetween(
//        request.indexInfoIds(),
//        TimeUtils.instantFromDashedDate(request.baseDateFrom()),
//        TimeUtils.instantFromDashedDate(request.baseDateTo())
//    );
//
//    log.info("indexDataList 성공적으로 불러옴 -> 총 불러온 수: {}", indexDataList.size());
//
//    // Request에서 받은 indexInfoId를 가진 지수데이터를 Map에 저장합니다.
//    // 추후 외부 api에서 받은 값이랑 비교하여 해당 맵에 없다면 만들 필요가 없는 데이터입니다.
//    Map<String, IndexData> existingIndexDataMap = indexDataList.stream()
//        .collect(Collectors.toMap(
//            indexData -> indexData.getIndexInfo().getId() + "|" + TimeUtils.formatedTimeString(
//                indexData.getBaseDate()),
//            indexData -> indexData
//        ));
//    log.info("지수 정보에 딸려 있는 지수 데이터의 갯수 : {}", existingIndexDataMap.keySet().size());
//
//    // API 가져옵니다.
//    List<IndexDataFromApi> apiResponse = scheduledTasks.fetchIndexData(
//        TimeUtils.formatedStringFromDashedDate(request.baseDateFrom()),
//        TimeUtils.formatedStringFromDashedDate(request.baseDateTo()));
//    log.info("index data api 성공적으로 불러옴 -> 총 불러온 수 : {}", apiResponse.size());
//
//    List<SyncJobHistoryDto> result = new ArrayList<>();
//
//    for (IndexDataFromApi indexDataFromApi : apiResponse) {
//      //해당하는 지수 정보 찾기
//      Optional<IndexInfo> optionalIndexInfo = indexInfoRepository.findByIndexClassificationAndIndexName(
//          indexDataFromApi.indexClassification(),
//          indexDataFromApi.indexName()
//      );
//
//      if (optionalIndexInfo.isEmpty()) { // 없으면 Skip
//        log.warn("IndexInfo를 찾을 수 없음: classification={}, name={}",
//            indexDataFromApi.indexClassification(), indexDataFromApi.indexName());
//        continue;
//      }
//
//      IndexInfo indexInfo = optionalIndexInfo.get();
//
//      if (request.indexInfoIds().contains(indexInfo.getId())) {
//        String key =
//            indexInfo.getId() + "|" + TimeUtils.normalizeToDashedDate(indexDataFromApi.baseDate());
//        if (!existingIndexDataMap.containsKey(key)) {
//          indexDataService.create(
//              IndexDataCreateRequest.from(indexInfo.getId(), indexDataFromApi),
//              SourceType.OPEN_API);
//
//          log.info("신규 IndexData 생성: indexInfoId={}, baseDate={}", indexInfo.getId(),
//              indexDataFromApi.baseDate());
//
//          SyncJobHistory syncJobHistory = syncJobHistoryService.saveHistory(
//              SyncJobHistoryCreateDto.forIndexData(indexInfo, ip,
//                  TimeUtils.formatedTimeInstantFromApi(indexDataFromApi.baseDate())));
//
//          result.add(SyncJobHistoryMapper.toDto(syncJobHistory));
//        }
//      }
//    }
//    log.info("연동된 총 갯수 {}", result.size());
//    return result;
//  }

  @Transactional
  @Override
  public List<SyncJobHistoryDto> syncIndexData(IndexDataSyncRequest request, String ip) {
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
          TimeUtils.formatedStringFromDashedDate(request.baseDateFrom()),
          TimeUtils.formatedStringFromDashedDate(request.baseDateTo())
      );
      log.info("생성된 api 데이터 목록의 수 : {}", apiResponse.size());

      Long indexInfoId = indexInfo.getId();
      //지수 데이터를 하나씩 순회를 시작한다.
      for (IndexDataFromApi indexDataFromApi : apiResponse) {
        Instant baseDateFromApi = TimeUtils.formatedTimeInstantFromApi(indexDataFromApi.baseDate());
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
              IndexDataCreateRequest.from(indexInfoId, indexDataFromApi),
              SourceType.OPEN_API);
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
