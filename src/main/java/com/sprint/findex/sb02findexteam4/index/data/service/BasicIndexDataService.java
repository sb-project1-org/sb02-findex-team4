package com.sprint.findex.sb02findexteam4.index.data.service;

import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_ALREADY_EXISTS;
import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_NOT_FOUND;
import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_INFO_NOT_FOUND;

import com.sprint.findex.sb02findexteam4.exception.AlreadyExistsException;
import com.sprint.findex.sb02findexteam4.exception.NotFoundException;
import com.sprint.findex.sb02findexteam4.index.data.IndexData;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.data.repository.IndexDataRepository;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.index.info.repository.IndexInfoRepository;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicIndexDataService implements IndexDataService {

  private final IndexDataRepository indexDataRepository;
  private final IndexInfoRepository indexInfoRepository;

  @Transactional
  @Override
  public IndexDataResponse create(IndexDataCreateRequest request, SourceType sourceType) {
    IndexInfo indexInfo = indexInfoRepository.findById(request.indexInfoId())
        .orElseThrow(() -> new NotFoundException(INDEX_INFO_NOT_FOUND));

    Instant instant = TimeUtils.formatedTimeInstant(request.baseDate());

    if (isDuplicated(request.indexInfoId(), instant)) {
      throw new AlreadyExistsException(INDEX_DATA_ALREADY_EXISTS);
    }

    IndexData indexData = IndexData.of(request, instant, indexInfo, sourceType);
    IndexData createdIndexData = indexDataRepository.save(indexData);


    return IndexDataResponse.from(createdIndexData);
  }

  @Transactional
  @Override
  public IndexDataResponse update(Long id, IndexDataUpdateRequest request) {
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(INDEX_DATA_NOT_FOUND));

    IndexData createdIndexData = indexDataRepository.save(indexData.update(request));
    return IndexDataResponse.from(createdIndexData);
  }

  @Override
  public void delete(Long id){
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(INDEX_DATA_NOT_FOUND));

    indexDataRepository.delete(indexData);
  }

  @Override
  public boolean isDuplicated(Long indexInfoId, Instant baseDate) {
    return indexDataRepository.existsByIndexInfoIdAndBaseDate(indexInfoId, baseDate);
  }
}
