package com.sprint.findex.sb02findexteam4.sync.dto.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

public record Item(
    @JsonFormat(pattern = "yyyyMMdd")
    LocalDate basPntm,
    String basIdx,
    @JsonFormat(pattern = "yyyyMMdd")
    LocalDate basDt,
    String idxCsf,
    String idxNm,
    Integer epyItmsCnt,
    BigDecimal vs,
    BigDecimal fltRt,
    BigDecimal mkp,
    BigDecimal clpr,
    BigDecimal hipr,
    BigDecimal lopr,
    Long trqu,
    Long trPrc,
    Long lstgMrktTotAmt
) {

}
