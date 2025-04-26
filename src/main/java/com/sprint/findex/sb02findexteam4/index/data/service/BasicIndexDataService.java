package com.sprint.findex.sb02findexteam4.index.data.service;

import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_ALREADY_EXISTS;
import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_NOT_FOUND;
import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_INFO_NOT_FOUND;

import com.sprint.findex.sb02findexteam4.exception.AlreadyExistsException;
import com.sprint.findex.sb02findexteam4.exception.NotFoundException;
import com.sprint.findex.sb02findexteam4.index.data.dto.ChartPoint;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexChartDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.RankedIndexPerformanceDto;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicIndexDataService implements IndexDataService {

  private final IndexDataRepository indexDataRepository;
  private final IndexInfoRepository indexInfoRepository;

  static final int MA5DATA_NUM = 5;
  static final int MA20DATA_NUM = 20;

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

  @Transactional(readOnly = true)
  @Override
  public IndexChartDto getIndexChart(Long indexInfoId, PeriodType periodType) {
    IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
        .orElseThrow(() -> new NotFoundException(INDEX_INFO_NOT_FOUND));

    LocalDate startDate = calculateBaseDate(periodType);
    LocalDate currentDate = Instant.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDate();

    List<ChartPoint> pricePoints = new ArrayList<>();

    while (!startDate.isAfter(currentDate)) {
      Optional<IndexData> dataOpt = indexDataRepository
          .findByIndexInfoIdAndBaseDateOnlyDateMatch(indexInfoId, startDate);

      LocalDate finalStartDate = startDate;
      dataOpt.ifPresent(data ->
          pricePoints.add(new ChartPoint(finalStartDate.toString(), data.getClosingPrice()))
      );

      startDate = startDate.plusDays(1);
    }

    List<ChartPoint> ma5 = calculateMovingAverageStrict(pricePoints, MA5DATA_NUM);
    List<ChartPoint> ma20 = calculateMovingAverageStrict(pricePoints, MA20DATA_NUM);

    return new IndexChartDto(pricePoints, ma5, ma20);
  }

  @Transactional(readOnly = true)
  @Override
    public List<IndexPerformanceDto> getFavoriteIndexPerformances(PeriodType periodType) {
    List<IndexInfo> favorites = indexInfoRepository.findAllByFavoriteTrue();
    LocalDate today = Instant.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDate();

    return favorites.stream()
        .map(indexInfo -> {
          IndexData current = indexDataRepository.findByIndexInfoIdAndBaseDateOnlyDateMatch(
              indexInfo.getId(), today
          ).orElse(null);
          IndexData past = indexDataRepository.findByIndexInfoIdAndBaseDateOnlyDateMatch(
                  indexInfo.getId(),
                  calculateBaseDate(periodType))
              .orElse(null);
          return IndexPerformanceDto.of(indexInfo, current, past);
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  @Override
  public List<RankedIndexPerformanceDto> getIndexPerformanceRank(Long indexInfoId,
      PeriodType periodType, int limit) {
    LocalDate baseDate = calculateBaseDate(periodType);
    LocalDate today = Instant.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDate();

    List<IndexInfo> targetInfos = (indexInfoId != null)
        ? indexInfoRepository.findAllById(List.of(indexInfoId))
        : indexInfoRepository.findAll();

    List<IndexPerformanceDto> sortedList = targetInfos.stream()
        .map(info -> {
          IndexData current = indexDataRepository
              .findByIndexInfoIdAndBaseDateOnlyDateMatch(info.getId(), today)
              .orElse(null);

          IndexData before = indexDataRepository
              .findByIndexInfoIdAndBaseDateOnlyDateMatch(info.getId(), baseDate)
              .orElse(null);

          return IndexPerformanceDto.of(info, current, before);
        })
        .filter(dto -> dto != null && dto.fluctuationRate() != null)
        .sorted(Comparator.comparing(IndexPerformanceDto::fluctuationRate).reversed())
        .limit(limit)
        .toList();

    List<RankedIndexPerformanceDto> rankedList = new ArrayList<>();
    for (int i = 0; i < sortedList.size(); i++) {
      rankedList.add(new RankedIndexPerformanceDto(sortedList.get(i),i + 1));
    }

    return rankedList;
  }

  @Override
  public void delete(Long id) {
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(INDEX_DATA_NOT_FOUND));

    indexDataRepository.delete(indexData);
  }

  private List<ChartPoint> calculateMovingAverageStrict(List<ChartPoint> prices, int window) {
    List<ChartPoint> result = new ArrayList<>();

    Map<LocalDate, BigDecimal> priceMap = prices.stream()
        .collect(Collectors.toMap(
            p -> LocalDate.parse(p.date()),
            ChartPoint::value
        ));

    List<LocalDate> sortedDates = prices.stream()
        .map(p -> LocalDate.parse(p.date()))
        .sorted()
        .toList();

    for (LocalDate baseDate : sortedDates) {
      List<LocalDate> pastDates = new ArrayList<>();
      for (int i = 1; i <= window; i++) {
        pastDates.add(baseDate.minusDays(i));
      }

      if (pastDates.stream().allMatch(priceMap::containsKey)) {
        BigDecimal sum = pastDates.stream()
            .map(priceMap::get)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avg = sum.divide(BigDecimal.valueOf(window), 2, RoundingMode.HALF_UP);
        result.add(new ChartPoint(baseDate.toString(), avg));
      }
    }

    return result;
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
      case QUARTERLY -> today.minusMonths(3);
      case YEARLY -> today.minusYears(1);
    };
  }
}
