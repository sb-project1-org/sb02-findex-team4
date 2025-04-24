package com.sprint.findex.sb02findexteam4.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

  public static String formatedTimeString(Instant instant) {
    return ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"))
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  public static Instant formatedTimeInstant(String string) {
    LocalDate localDate = LocalDate.parse(string);
    return localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
  }
}
