package com.sprint.findex.sb02findexteam4.index.data.service;

import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_ALREADY_EXISTS;
import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_NOT_FOUND;
import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_INFO_NOT_FOUND;

import com.sprint.findex.sb02findexteam4.exception.AlreadyExistsException;
import com.sprint.findex.sb02findexteam4.exception.NotFoundException;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.data.entity.PeriodType;
import com.sprint.findex.sb02findexteam4.index.data.repository.IndexDataRepository;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.index.info.repository.IndexInfoRepository;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
  public List<IndexPerformanceDto> getFavoriteIndexPerformances(PeriodType periodType) {
    List<IndexInfo> favorites = indexInfoRepository.findAllByFavoriteTrue();
    LocalDate today = Instant.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDate();

    return favorites.stream()
        .map(indexInfo -> {
          IndexData current = indexDataRepository.findByIndexInfoIdAndBaseDateOnlyDateMatch(
              indexInfo.getId(), today
          ).orElse(null);
          IndexData past = indexDataRepository.findByIndexInfoIdAndBaseDateOnlyDateMatch(indexInfo.getId(),
                  calculateBaseDate(periodType))
              .orElse(null);
          return IndexPerformanceDto.of(indexInfo, current, past);
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Long id) {
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(INDEX_DATA_NOT_FOUND));

    indexDataRepository.delete(indexData);
  }

  @Override
  public boolean isDuplicated(Long indexInfoId, Instant baseDate) {
    return indexDataRepository.existsByIndexInfoIdAndBaseDate(indexInfoId, baseDate);
  }

  private LocalDate calculateBaseDate(PeriodType periodType) {
    LocalDate today = Instant.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDate();

    return switch (periodType) {
      case DAILY -> today.minusDays(1);
      case WEEKLY -> today.minusWeeks(1);
      case MONTHLY -> today.minusMonths(1);
    };
  }
}
