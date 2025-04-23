package com.sprint.findex.sb02findexteam4.indexData.service;

import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_ALREADY_EXISTS;
import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_INFO_NOT_FOUND;

import com.sprint.findex.sb02findexteam4.exception.AlreadyExistsException;
import com.sprint.findex.sb02findexteam4.exception.NotFoundException;
import com.sprint.findex.sb02findexteam4.indexData.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.indexData.dto.IndexDataCreateResponse;
import com.sprint.findex.sb02findexteam4.indexData.IndexData;
import com.sprint.findex.sb02findexteam4.indexData.repository.IndexDataRepository;
import com.sprint.findex.sb02findexteam4.indexInfo.IndexInfo;
import com.sprint.findex.sb02findexteam4.indexInfo.IndexInfoRepository;
import com.sprint.findex.sb02findexteam4.indexInfo.SourceType;
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
  public IndexDataCreateResponse create(IndexDataCreateRequest request, SourceType sourceType){
    IndexInfo indexInfo = indexInfoRepository.findById(request.indexInfoId())
        .orElseThrow(() -> new NotFoundException(INDEX_INFO_NOT_FOUND));

    if (isDuplicated(request.indexInfoId(), request.baseDate())) {
      throw new AlreadyExistsException(INDEX_DATA_ALREADY_EXISTS);
    }

    IndexData indexData = IndexData.of(request, indexInfo, sourceType);
    IndexData createdIndexData = indexDataRepository.save(indexData);
    return IndexDataCreateResponse.from(createdIndexData);
  }

  @Override
  public boolean isDuplicated(Long indexInfoId, Instant baseDate) {
    return indexDataRepository.existsByIndexInfoIdAndBaseDate(indexInfoId, baseDate);
  }
}
