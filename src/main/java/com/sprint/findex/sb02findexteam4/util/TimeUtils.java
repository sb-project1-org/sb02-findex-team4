package com.sprint.findex.sb02findexteam4.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
   * <h2>Instant -> String 타입 yyyy-MM-dd 변환</h2>
   * Instant 타입을 yyyy-MM-dd타입으로 변환한다.
   *
   * @param instant 글로벌 타임
   * @return String 타입의 yyyy-MM-dd
   */
  public static String formatedTimeString(Instant instant) {
    return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
        .format(yyyy_MM_dd);
  }

  /**
   * <h2>Instant -> String 타입 yyyyMMdd 변환 </h2>
   * Instant 타입을 yyyyMMdd타입으로 변환한다.
   *
   * @param instant 글로벌 타임
   * @return yyyyMMdd
   */
  public static String formatedTimeStringNonDashed(Instant instant) {
    return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
        .format(yyyyMMdd);
  }

  /**
   * <h2>String: yyyy-MM-dd -> Instant 타입 변환</h2>
   * yyyy-MM-dd를 Instant 타입으로 변환한다. 외부 입력을 받아서 DB에 저장할 때 사용한다.
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
   * yyyyMMdd String을 Instant 타입으로 변환한다. 외부 API를 받아서 DB에 저장할 때 사용한다.
   *
   * @param string yyyyMMdd
   * @return Instant타입
   */
  public static Instant formatedTimeInstantFromApi(String string) {
    string = string.trim();
    LocalDate date = LocalDate.parse(string, yyyyMMdd);
    return date.atStartOfDay(ZoneOffset.UTC).toInstant();
  }

  /**
   * <h2>현재 시각 기준, 한 달 전 yyyy-MM-dd 반환</h2>
   * 현재 시각 기준으로 한 달 전을 반환해주는 메서드
   *
   * @return Instant 타입, yyyy-MM-dd
   */
  public static Instant oneMonthAgoDateToString() {
    ZonedDateTime oneMonthAgo = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusMonths(1);
    //yyyy-mm-dd 포맷
    return oneMonthAgo.toInstant();
  }

}
