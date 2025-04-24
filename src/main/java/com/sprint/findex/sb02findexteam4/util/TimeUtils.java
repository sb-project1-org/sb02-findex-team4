package com.sprint.findex.sb02findexteam4.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

  public static String formatedTimeString(Instant instant) {
    return ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"))
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  public static Instant formatedTimeInstant(String string) {
    DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyyMMdd");
    string = string.trim();
    LocalDate date = LocalDate.parse(string, YMD);
    return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
  }
}
