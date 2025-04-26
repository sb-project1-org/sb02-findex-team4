package com.sprint.findex.sb02findexteam4.sync.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.ExternalApiException;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.IndexDataFromApi;
import com.sprint.findex.sb02findexteam4.sync.dto.MarketIndexResponse;
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

  private String encodeServiceKey;

  @PostConstruct
  void init() {
    encodeServiceKey = URLEncoder.encode(rawServiceKey, StandardCharsets.UTF_8);
  }

  public List<IndexInfoCreateRequest> fetchIndexInfo() {
    try {
      HttpResponse<String> response = getResponse(null, null);
      MarketIndexResponse indexResponse = mapper.readValue(response.body(),
          MarketIndexResponse.class);
      List<IndexInfoCreateRequest> infoRequests = IndexInfoCreateFromResponse(
          indexResponse);
      log.info("수동 info 배치 성공 ");
      return infoRequests;
    } catch (Exception e) {
      log.error("지수 정보 수집 실패");
      throw new ExternalApiException(ErrorCode.EXTERNAL_API_BAD_GATE_WAY);
    }
  }

  public List<IndexDataFromApi> fetchIndexData(String bastDateFrom, String baseDateTo) {
    try {
      HttpResponse<String> response = getResponse(bastDateFrom, baseDateTo);

      MarketIndexResponse indexResponse = mapper.readValue(response.body(),
          MarketIndexResponse.class);

      List<IndexDataFromApi> dataRequests = IndexDataCreateFromResponse(indexResponse);
      log.info("수동 data 배치 성공 ");

      return dataRequests;
    } catch (Exception e) {
      log.error("지수 데이터 수집 실패");
      throw new ExternalApiException(ErrorCode.EXTERNAL_API_BAD_GATE_WAY);
    }
  }

  private HttpResponse<String> getResponse(String bastDateFrom, String baseDateTo) {
    try {
      URI uri;
      if (bastDateFrom == null && baseDateTo == null) {
        uri = getUri();
      } else {
        uri = getUri(bastDateFrom, baseDateTo);
      }

      HttpRequest request = HttpRequest.newBuilder()
          .uri(uri)
          .timeout(Duration.ofSeconds(10))
          .GET()
          .build();
      HttpResponse<String> response = HttpClient.newHttpClient()
          .send(request, BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        log.info("response 성공적으로 불러옴");
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
    UriComponentsBuilder uriComponentsBuilder = getUriComponent();
    return toUri(uriComponentsBuilder);
  }

  private UriComponentsBuilder getUriComponent() {
    return UriComponentsBuilder.fromHttpUrl(BASE_URL)
        .queryParam("serviceKey", encodeServiceKey)
        .queryParam("resultType", "json")
        .queryParam("pageNo", 1)
        .queryParam("numOfRows", 10);
  }

  private URI getUri(String bastDateFrom, String baseDateTo) {
    UriComponentsBuilder uriComponentsBuilder = getUriComponent();
    uriComponentsBuilder.queryParam("beginBasDt", bastDateFrom)
        .queryParam("endBasDt", baseDateTo);
    return toUri(uriComponentsBuilder);
  }

  private URI toUri(UriComponentsBuilder uriComponentsBuilder) {
    return uriComponentsBuilder.build(true).toUri();
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
