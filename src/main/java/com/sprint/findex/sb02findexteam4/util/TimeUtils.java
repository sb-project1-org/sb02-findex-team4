package com.sprint.findex.sb02findexteam4.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * <h2>Time Util</h2>
 * yyyyMMdd 또는 yyyy-mm-dd로 바꾸는 기능이 정의된 클래스
 */
public class TimeUtils {

  //yyyyMMdd 포맷
  public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

  //yyyy-MM-dd 포맷
  public static final DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * <h2>Instant -> String 타입 변환</h2>
   * Instant 타입을 yyyy-MM-dd타입으로 변환한다.
   *
   * @param instant 글로벌 타임
   * @return yyyy-MM-dd
   */
  public static String formatedTimeString(Instant instant) {
    return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
        .format(yyyy_MM_dd);
  }

  /**
   * <h2>String -> Instant 타입 변환: yyyy-MM-dd</h2>
   * yyyy-MM-dd T 00:00:00을 Instant 타입으로 변환한다.
   *
   * @param string yyyy-MM-dd
   * @return yyyy-MM-dd 00:00:00.000000
   */
  public static Instant formatedTimeInstant(String string) {
    LocalDate localDate = LocalDate.parse(string);
    return localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
  }

  /**
   * <h2>String -> Instant 타입 변환: yyyyMMdd</h2>
   * yyyyMMdd String을 Instant 타입으로 변환한다.
   *
   * @param string yyyyMMdd
   * @return Instant타입
   */
  public static Instant formatedTimeInstantFromApi(String string) {
    string = string.trim();
    LocalDate date = LocalDate.parse(string, yyyyMMdd);
    return date.atStartOfDay(ZoneOffset.UTC).toInstant();
  }

//  public static Instant oneMonthAgoDateToInstant() {
//    LocalDate now = LocalDate.now();
//    LocalDate date = now.minusMonths(1);
//    return date.atStartOfDay(ZoneOffset.UTC).toInstant();
//  }

  /**
   * <h2>현재 시각 기준, 한 달 전 yyyy-MM-dd 반환</h2>
   * 현재 시각 기준으로 한 달 전을 반환해주는 메서드
   *
   * @return String 타입, yyyy-MM-dd
   */
  public static String oneMonthAgoDateToString() {
    ZonedDateTime oneMonthAgo = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusMonths(1);
    //yyyy-mm-dd 포맷
    return oneMonthAgo.toLocalDate().format(yyyy_MM_dd);
  }

  //  public static String formatedStringToDash(String rawDate) {
//    LocalDate date = LocalDate.parse(rawDate.trim(), yyyyMMdd);
//    return date.format(yyyy_MM_dd);
//  }
//
//  public static String toSimpleDate(String isoDate) {
//    OffsetDateTime parse = OffsetDateTime.parse(isoDate);
//    return parse.toLocalDate().format(yyyy_MM_dd);
//  }

  /**
   * <h2>yyyy-MM-dd -> yyyyMMdd 변환 메서드</h2>
   * String 타입, yyyy-MM-dd 날짜를 String 타입, yyyyMMdd로 변환
   *
   * @param dashedDate String, yyyy-MM-dd
   * @return String yyyyMMdd
   */
  public static String formatedStringFromDashedDate(String dashedDate) {
    LocalDate date = LocalDate.parse(dashedDate.trim(), yyyy_MM_dd);
    return date.format(yyyyMMdd);
  }

  /**
   * <h2>yyyy-MM-dd, yyyyMMdd, yyyy-MM-dd T 00:00:00 -> yyyy-MM-dd로 통일되게 만드는 메서드</h2>
   *
   * @param input String으로 되어있는 모든 시간 표현
   * @return String yyyy-MM-dd
   */
  public static String normalizeToDashedDate(String input) {
    input = input.trim();  // 공백 제거

    try {
      // 1. yyyyMMdd
      if (input.matches("^\\d{8}$")) {
        LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return date.format(yyyy_MM_dd);
      }

      // 2. yyyy-MM-ddTHH:mm:ss.SSS...+09:00 (OffsetDateTime)
      if (input.contains("T") && input.contains("+")) {
        OffsetDateTime odt = OffsetDateTime.parse(input);
        return odt.toLocalDate().format(yyyy_MM_dd);
      }

      // 3. yyyy-MM-dd
      if (input.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
        return input;
      }

    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("지원하지 않는 날짜 형식: " + input, e);
    }

    throw new IllegalArgumentException("날짜 형식을 인식할 수 없습니다: " + input);
  }

  /**
   * <h2>yyyy-MM-dd -> Instant 변환 메서드 (UTC 기준)</h2>
   * String 타입의 yyyy-MM-dd 날짜를 Instant로 변환합니다.
   * 시간은 자정(00:00:00) 기준이며, 시간대는 UTC입니다.
   *
   * @param dashedDate String, yyyy-MM-dd
   * @return Instant UTC 기준 00:00:00의 Instant 객체
   */
  public static Instant instantFromDashedDate(String dashedDate) {
    LocalDate date = LocalDate.parse(dashedDate.trim(), yyyy_MM_dd);
    return date.atStartOfDay(ZoneOffset.UTC).toInstant();
  }

}
