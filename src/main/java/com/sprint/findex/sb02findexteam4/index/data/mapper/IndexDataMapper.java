package com.sprint.findex.sb02findexteam4.index.data.mapper;

import com.sprint.findex.sb02findexteam4.config.GlobalMapperConfig;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface IndexDataMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "indexInfo", source = "indexInfo")
  @Mapping(target = "baseDate", source = "request.baseDate")
  @Mapping(target = "sourceType", source = "sourceType")
  @Mapping(target = "marketPrice", source = "request.marketPrice")
  @Mapping(target = "closingPrice", source = "request.closingPrice")
  @Mapping(target = "highPrice", source = "request.highPrice")
  @Mapping(target = "lowPrice", source = "request.lowPrice")
  @Mapping(target = "versus", source = "request.versus")
  @Mapping(target = "fluctuationRate", source = "request.fluctuationRate")
  @Mapping(target = "tradingQuantity", source = "request.tradingQuantity")
  @Mapping(target = "tradingPrice", source = "request.tradingPrice")
  @Mapping(target = "marketTotalAmount", source = "request.marketTotalAmount")
  IndexData toEntity(IndexDataCreateRequest request, IndexInfo indexInfo, SourceType sourceType);

  @Mapping(target = "id", source = "indexData.id")
  @Mapping(target = "indexInfoId", source = "indexData.indexInfo.id")
  @Mapping(target = "baseDate", source = "indexData.baseDate")
  @Mapping(target = "sourceType", source = "indexData.sourceType")
  @Mapping(target = "marketPrice", source = "indexData.marketPrice")
  @Mapping(target = "closingPrice", source = "indexData.closingPrice")
  @Mapping(target = "highPrice", source = "indexData.highPrice")
  @Mapping(target = "lowPrice", source = "indexData.lowPrice")
  @Mapping(target = "versus", source = "indexData.versus")
  @Mapping(target = "fluctuationRate", source = "indexData.fluctuationRate")
  @Mapping(target = "tradingQuantity", source = "indexData.tradingQuantity")
  @Mapping(target = "tradingPrice", source = "indexData.tradingPrice")
  @Mapping(target = "marketTotalAmount", source = "indexData.marketTotalAmount")
  IndexDataResponse toResponse(IndexData indexData);

}
