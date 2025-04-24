package com.sprint.findex.sb02findexteam4.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final ObjectMapper mapper;
    private static final String BASE_URL = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex";

    @Value("${openApi.key}")
    private String rawServiceKey;

    private String encodeServiceKey;

    @PostConstruct
    void init() {
        encodeServiceKey = URLEncoder.encode(rawServiceKey, StandardCharsets.UTF_8);
    }

//    @Scheduled(fixedDelay = 30000)
//    private void fetch() {
//        try {
//            URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
//                .queryParam("serviceKey", encodeServiceKey)
//                .queryParam("resultType", "json")
//                .queryParam("pageNo", 1)
//                .queryParam("numOfRows", "10")
//                .build(true)
//                .toUri();
//
//            HttpRequest request = HttpRequest.newBuilder()
//                .uri(uri)
//                .timeout(Duration.ofSeconds(10))
//                .GET()
//                .build();
//
//            HttpResponse<String> response = HttpClient.newHttpClient()
//                .send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() == 200) {
//                log.info("response : {}", response.body());
////                MarketIndexResponse indexResponse = objectMapper.readValue(response.body(),
////                    MarketIndexResponse.class);
//
//            } else {
//                log.warn("API 호출 오류 code={}, body={}", response.statusCode(), response.body());
//            }
//        } catch (HttpTimeoutException e) {
//            throw new ExternalApiException(ErrorCode.EXTERNAL_API_TIMEOUT);
//        } catch (Exception e) {
//            log.error("지수 정보 수집 실패", e);
//        }
//    }
}
