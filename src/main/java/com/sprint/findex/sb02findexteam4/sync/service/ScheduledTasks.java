package com.sprint.findex.sb02findexteam4.sync.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.ExternalApiException;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.api.IndexDataFromApi;
import com.sprint.findex.sb02findexteam4.sync.dto.api.MarketIndexResponse;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

  private final ObjectMapper mapper;
  private static final String BASE_URL = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex";
  @Value("${openApi.key}")
  private String rawServiceKey;

  private static final int MAX_API_COUNT = 250;

  private String encodeServiceKey;

  @PostConstruct
  void init() {
    encodeServiceKey = URLEncoder.encode(rawServiceKey, StandardCharsets.UTF_8);
  }

  public List<IndexInfoCreateRequest> fetchIndexInfo() {
    try {
      HttpResponse<String> response = getResponse(null, null, null);
      MarketIndexResponse indexResponse = mapper.readValue(response.body(),
          MarketIndexResponse.class);
      List<IndexInfoCreateRequest> infoRequests = IndexInfoCreateFromResponse(
          indexResponse);
      return infoRequests;
    } catch (Exception e) {
      log.error("[ScheduledTask] fetchIndexInfo - failed Method");
      throw new ExternalApiException(ErrorCode.EXTERNAL_API_BAD_GATE_WAY);
    }
  }

  /**
   * <h2>api indexData 요청 </h2>
   * api를 향해 index Data를 요청합니다.
   *
   * @param indexName    타겟이 되는 인덱스 이름
   * @param baseDateFrom Instant 타입의 시작일자
   * @param baseDateTo   Insatant 타입의 종료일자
   * @return indexData 를 반환합니다.
   */
  public List<IndexDataFromApi> fetchIndexData(String indexName, Instant baseDateFrom,
      Instant baseDateTo) {
    String stringBaseDateFrom = TimeUtils.formatedTimeStringNonDashed(baseDateFrom);
    String stringBaseDateTo = TimeUtils.formatedTimeStringNonDashed(baseDateTo);
    try {
      HttpResponse<String> response = getResponse(indexName, stringBaseDateFrom, stringBaseDateTo);
      MarketIndexResponse indexResponse = mapper.readValue(response.body(),
          MarketIndexResponse.class);
      List<IndexDataFromApi> dataRequests = IndexDataCreateFromResponse(indexResponse);
      log.info("[ScheduledTask] fetchIndexData - call Api Response");
      return dataRequests;
    } catch (Exception e) {
      log.error("[ScheduledTask] fetchIndexData - failed Method");
      throw new ExternalApiException(ErrorCode.EXTERNAL_API_BAD_GATE_WAY);
    }
  }

  private HttpResponse<String> getResponse(String indexName, String bastDateFrom,
      String baseDateTo) {
    try {
      URI uri;
      if (bastDateFrom == null && baseDateTo == null) {
        uri = getUri();
      } else {
        uri = getUri(indexName, bastDateFrom, baseDateTo);
      }

      HttpRequest request = HttpRequest.newBuilder()
          .uri(uri)
          .timeout(Duration.ofSeconds(10))
          .GET()
          .build();

      HttpResponse<String> response = HttpClient.newHttpClient()
          .send(request, BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        return response;
      } else {
        log.warn("API 호출 오류 code={}, body={}", response.statusCode(), response.body());
        throw new ExternalApiException(ErrorCode.EXTERNAL_API_BAD_GATE_WAY);
      }
    } catch (IOException | InterruptedException e) {
      throw new ExternalApiException(ErrorCode.EXTERNAL_API_BAD_GATE_WAY);
    }
  }

  private URI getUri() {
    return UriComponentsBuilder.fromHttpUrl(BASE_URL)
        .queryParam("serviceKey", encodeServiceKey)
        .queryParam("resultType", "json")
        .queryParam("pageNo", 1)
        .queryParam("numOfRows", MAX_API_COUNT)
        .build(true).toUri();
  }

  private URI getUri(String indexName, String bastDateFrom, String baseDateTo) {
    String encodedIndexName = URLEncoder.encode(indexName, StandardCharsets.UTF_8);

    return UriComponentsBuilder.fromHttpUrl(BASE_URL)
        .queryParam("serviceKey", encodeServiceKey)
        .queryParam("resultType", "json")
        .queryParam("pageNo", 1)
        .queryParam("numOfRows", MAX_API_COUNT)
        .queryParam("idxNm", encodedIndexName)
        .queryParam("beginBasDt", bastDateFrom)
        .queryParam("endBasDt", baseDateTo)
        .build(true).toUri();
  }

  private List<IndexInfoCreateRequest> IndexInfoCreateFromResponse(
      MarketIndexResponse indexResponse) {
    return indexResponse.response().body().items().item().stream().map(IndexInfoCreateRequest::of)
        .toList();
  }

  private List<IndexDataFromApi> IndexDataCreateFromResponse(
      MarketIndexResponse indexResponse) {
    return indexResponse.response().body().items().item().stream().map(IndexDataFromApi::of)
        .toList();
  }
}
