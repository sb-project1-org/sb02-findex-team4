package com.sprint.findex.sb02findexteam4.index.data.dto;

import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;

@Builder
public record IndexDataCreateCommand(
    Long indexInfoId,
    Instant baseDate,
    BigDecimal marketPrice,
    BigDecimal closingPrice,
    BigDecimal highPrice,
    BigDecimal lowPrice,
    BigDecimal versus,
    BigDecimal fluctuationRate,
    Long tradingQuantity,
    Long tradingPrice,
    Long marketTotalAmount,
    SourceType sourceType
) {

//  public static IndexDataCreateCommand fromUser(IndexDataCreateRequest request,
//      SourceType sourceType) {
//    return IndexDataCreateCommand.builder()
//        .indexInfoId(request.indexInfoId())
//        .baseDate(TimeUtils.formatedTimeInstant(request.baseDate()))
//        .marketPrice(request.marketPrice())
//        .closingPrice(request.closingPrice())
//        .highPrice(request.highPrice())
//        .lowPrice(request.lowPrice())
//        .versus(request.versus())
//        .fluctuationRate(request.fluctuationRate())
//        .tradingQuantity(request.tradingQuantity())
//        .tradingPrice(request.tradingPrice())
//        .marketTotalAmount(request.marketTotalAmount())
//        .sourceType(sourceType).build();
//  }
//
//  public static IndexDataCreateCommand fromApi(Long indexInfoId, IndexDataFromApi request,
//      SourceType sourceType) {
//    return IndexDataCreateCommand.builder()
//        .indexInfoId(indexInfoId)
//        .baseDate(request.baseDate())
//        .marketPrice(request.marketPrice())
//        .closingPrice(request.closingPrice())
//        .highPrice(request.highPrice())
//        .lowPrice(request.lowPrice())
//        .versus(request.versus())
//        .fluctuationRate(request.fluctuationRate())
//        .tradingQuantity(request.tradingQuantity())
//        .tradingPrice(request.tradingPrice())
//        .marketTotalAmount(request.marketTotalAmount())
//        .sourceType(sourceType).build();
//  }
}
