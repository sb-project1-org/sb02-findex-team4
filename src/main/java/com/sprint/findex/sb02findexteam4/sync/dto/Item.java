package com.sprint.findex.sb02findexteam4.sync.dto;

import java.math.BigDecimal;

public record Item(
    String basPntm,
    String basIdx,
    String basDt,
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
    BigDecimal trPrc,
    BigDecimal lstgMrktTotAmt
) {

}
