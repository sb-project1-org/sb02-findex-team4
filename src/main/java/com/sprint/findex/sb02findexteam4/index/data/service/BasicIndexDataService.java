package com.sprint.findex.sb02findexteam4.index.data.service;

import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_ALREADY_EXISTS;
import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_DATA_NOT_FOUND;
import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_INFO_NOT_FOUND;

import com.sprint.findex.sb02findexteam4.exception.AlreadyExistsException;
import com.sprint.findex.sb02findexteam4.exception.NotFoundException;
import com.sprint.findex.sb02findexteam4.index.data.dto.ChartPoint;
import com.sprint.findex.sb02findexteam4.index.data.dto.CursorPageResponseIndexDataDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexChartDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateCommand;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCsvExportCommand;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataFindCommand;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.RankedIndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import com.sprint.findex.sb02findexteam4.index.data.entity.PeriodType;
import com.sprint.findex.sb02findexteam4.index.data.repository.IndexDataRepository;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.repository.IndexInfoRepository;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicIndexDataService implements IndexDataService {

  private final IndexDataRepository indexDataRepository;
  private final IndexInfoRepository indexInfoRepository;

  static final int MA5DATA_NUM = 5;
  static final int MA20DATA_NUM = 20;

  @Transactional
  @Override
  public IndexDataResponse create(IndexDataCreateCommand command) {
    IndexInfo indexInfo = indexInfoRepository.findById(command.indexInfoId())
        .orElseThrow(() -> new NotFoundException(INDEX_INFO_NOT_FOUND));

    if (isDuplicated(command.indexInfoId(), command.baseDate())) {
      throw new AlreadyExistsException(INDEX_DATA_ALREADY_EXISTS);
    }

    IndexData indexData = IndexData.from(indexInfo, command);
    IndexData createdIndexData = indexDataRepository.save(indexData);
    log.info("[BasicIndexDataService] Method create - {} ", indexData.getId());
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
  public CursorPageResponseIndexDataDto getIndexDataList(IndexDataFindCommand command) {
    List<IndexData> result = indexDataRepository.findWithConditions(command);
    boolean hasNext = result.size() > command.size();
    if (hasNext) {
      result.remove(result.size() - 1);
    }

    List<IndexDataDto> contents = result.stream()
        .map(IndexDataDto::from)
        .collect(Collectors.toList());

    Long nextIdAfter = contents.isEmpty() ? null : contents.get(contents.size() - 1).id();
    String nextCursor = nextIdAfter != null ? nextIdAfter.toString() : null;

    Long totalElements = indexDataRepository.countWithConditions(command);

    return new CursorPageResponseIndexDataDto(
        contents,
        nextCursor,
        nextIdAfter,
        command.size(),
        totalElements,
        hasNext
    );
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

    return new IndexChartDto(indexInfoId, indexInfo.getIndexClassification(),
        indexInfo.getIndexName(), periodType, pricePoints, ma5, ma20);
  }

  @Transactional(readOnly = true)
  @Override
  public List<IndexPerformanceDto> getFavoriteIndexPerformances(PeriodType periodType) {
    List<IndexInfo> favorites = indexInfoRepository.findAllByFavoriteTrue();
    
    //최신 날짜로 부터 1달 전의 값을 구해야 한다.
    return favorites.stream()
        .map(indexInfo -> {
          log.info("[BasicIndexDataService] method getFavoriteIndexPerformances, favorite: {} ",
              indexInfo.getIndexName());
          IndexData current = indexDataRepository.findTopByIndexInfoIdOrderByBaseDateDesc(
              indexInfo.getId()).orElse(null);

          IndexData past = indexDataRepository.findClosestTo(
                  indexInfo.getId(),
                  calculateBaseDate(periodType))
              .orElse(null);
          return IndexPerformanceDto.of(indexInfo, current,
              past);
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  @Override
  public List<RankedIndexPerformanceDto> getIndexPerformanceRank(Long indexInfoId,
      PeriodType periodType, int limit) {
    LocalDate baseDate = calculateBaseDate(periodType);

    List<IndexInfo> targetInfos = (indexInfoId != null)
        ? indexInfoRepository.findAllById(List.of(indexInfoId))
        : indexInfoRepository.findAll();

    List<IndexPerformanceDto> sortedList = targetInfos.stream()
        .map(info -> {
          IndexData current = indexDataRepository.findTopByIndexInfoIdOrderByBaseDateDesc(
              info.getId()).orElse(null);
          IndexData before = indexDataRepository
              .findClosestTo(info.getId(), baseDate)
              .orElse(null);

          return IndexPerformanceDto.of(info, current, before);
        })
        .filter(dto -> dto != null && dto.fluctuationRate() != null)
        .sorted(Comparator.comparing(IndexPerformanceDto::fluctuationRate).reversed())
        .limit(limit)
        .toList();

    List<RankedIndexPerformanceDto> rankedList = new ArrayList<>();
    for (int i = 0; i < sortedList.size(); i++) {
      rankedList.add(new RankedIndexPerformanceDto(sortedList.get(i), i + 1));
    }

    return rankedList;
  }

  @Override
  public byte[] exportCsv(IndexDataCsvExportCommand command) {
    List<IndexData> dataList = indexDataRepository.findAllForCsvExport(command);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8);

    writer.println(
        "baseDate,marketPrice,closingPrice,highPrice,lowPrice,versus,fluctuationRate,tradingQuantity,tradingPrice,marketTotalAmount");

    for (IndexData data : dataList) {
      writer.printf("%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%d,%d,%d\n",
          data.getBaseDate().toString(),
          data.getMarketPrice(),
          data.getClosingPrice(),
          data.getHighPrice(),
          data.getLowPrice(),
          data.getVersus(),
          data.getFluctuationRate(),
          data.getTradingQuantity(),
          data.getTradingPrice(),
          data.getMarketTotalAmount()
      );
    }
    writer.flush();
    return out.toByteArray();
  }

  @Override
  @Transactional
  public void delete(Long id) {
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(INDEX_DATA_NOT_FOUND));

    indexDataRepository.delete(indexData);
  }

  private List<ChartPoint> calculateMovingAverageStrict(List<ChartPoint> prices, int window) {

    // 날짜 오름차순 정렬
    List<ChartPoint> pts = prices.stream()
        .sorted(Comparator.comparing(p -> LocalDate.parse(p.date())))
        .toList();

    Deque<BigDecimal> win = new ArrayDeque<>(window);
    BigDecimal sum = BigDecimal.ZERO;
    List<ChartPoint> result = new ArrayList<>(pts.size());

    for (ChartPoint p : pts) {
      BigDecimal v = p.value();
      win.addLast(v);
      sum = sum.add(v);

      if (win.size() > window)             // 윈도 초과 시 맨 앞 제거
      {
        sum = sum.subtract(win.removeFirst());
      }

      BigDecimal avg = (win.size() == window)
          ? sum.divide(BigDecimal.valueOf(window), 2, RoundingMode.HALF_UP)
          : null;                      // 아직 데이터 부족

      result.add(new ChartPoint(p.date(), avg));
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
