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
import java.time.LocalDate;
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

      return indexResponse.response().body().items().item().stream().map(IndexInfoCreateRequest::of)
          .toList();
    } catch (Exception e) {
      log.error("[ScheduledTask] fetchIndexInfo - failed Method");
      throw new ExternalApiException(ErrorCode.EXTERNAL_API_BAD_GATE_WAY);
    }
  }

  public List<IndexDataFromApi> fetchIndexData(String indexName, LocalDate baseDateFrom,
      LocalDate baseDateTo) {
    try {
      HttpResponse<String> response = getResponse(indexName, baseDateFrom, baseDateTo);

      MarketIndexResponse indexResponse = mapper.readValue(response.body(),
          MarketIndexResponse.class);

      return indexResponse.response().body().items().item().stream().map(IndexDataFromApi::of)
          .toList();

    } catch (Exception e) {

      log.error("[ScheduledTask] fetchIndexData - failed Method");

      throw new ExternalApiException(ErrorCode.EXTERNAL_API_BAD_GATE_WAY);
    }
  }

  private HttpResponse<String> getResponse(String indexName, LocalDate bastDateFrom,
      LocalDate baseDateTo) {
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
    return UriComponentsBuilder.fromUriString(BASE_URL)
        .queryParam("serviceKey", encodeServiceKey)
        .queryParam("resultType", "json")
        .queryParam("pageNo", 1)
        .queryParam("numOfRows", MAX_API_COUNT)
        .build(true).toUri();
  }

  private URI getUri(String indexName, LocalDate baseDateFrom, LocalDate baseDateTo) {
    String stringBaseDateFrom = TimeUtils.formatedTimeStringNonDashed(baseDateFrom);
    String stringBaseDateTo = TimeUtils.formatedTimeStringNonDashed(baseDateTo);
    String encodedIndexName = URLEncoder.encode(indexName, StandardCharsets.UTF_8);

    return UriComponentsBuilder.fromUriString(BASE_URL)
        .queryParam("serviceKey", encodeServiceKey)
        .queryParam("resultType", "json")
        .queryParam("pageNo", 1)
        .queryParam("numOfRows", MAX_API_COUNT)
        .queryParam("idxNm", encodedIndexName)
        .queryParam("beginBasDt", stringBaseDateFrom)
        .queryParam("endBasDt", stringBaseDateTo)
        .build(true).toUri();
  }
}
