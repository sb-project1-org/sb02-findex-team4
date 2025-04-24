package com.sprint.findex.sb02findexteam4.indexInfo.service.impl;

import com.sprint.findex.sb02findexteam4.exception.ErrorCode;
import com.sprint.findex.sb02findexteam4.exception.NormalException;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoUpdateRequest;
import com.sprint.findex.sb02findexteam4.indexInfo.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.indexInfo.entity.SourceType;
import com.sprint.findex.sb02findexteam4.indexInfo.mapper.IndexInfoMapper;
import com.sprint.findex.sb02findexteam4.indexInfo.repository.IndexInfoRepository;
import com.sprint.findex.sb02findexteam4.indexInfo.service.IndexInfoService;
import com.sprint.findex.sb02findexteam4.indexInfo.service.IndexInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IndexInfoServiceImpl implements IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoValidator indexInfoValidator;

  @Autowired
  private IndexInfoMapper indexInfoMapper;

  public IndexInfoServiceImpl(IndexInfoRepository indexInfoRepository,
      IndexInfoValidator indexInfoValidator) {
    this.indexInfoRepository = indexInfoRepository;
    this.indexInfoValidator = indexInfoValidator;
  }

  @Override
  public IndexInfoDto registerIndexInfo(IndexInfoCreateRequest requestDto) {
    indexInfoValidator.validateForCreate(requestDto);

    SourceType sourceType = SourceType.USER;

    IndexInfo indexInfo = IndexInfo.create(requestDto, sourceType);
    indexInfoRepository.save(indexInfo);

    // 자동연동 엔티티 만들어야 합니다.

    IndexInfoDto indexInfoDto = new IndexInfoDto(
          indexInfo.getId(),
          indexInfo.getIndexClassification(),
          indexInfo.getIndexName(),
          indexInfo.getEmployedItemsCount(),
          indexInfo.getBasePointInTime(),
          indexInfo.getBaseIndex(),
          indexInfo.getSourceType(),
          indexInfo.getFavorite()
        );

    return indexInfoDto;
  }

  @Override
  public Page<IndexInfoDto> getIndexInfoWithFilters(String classificationName, String indexName,
      Boolean favorite, Pageable pageable) {
//    if (classificationName != null && indexName != null && favorite != null) {
//      return indexInfoRepository.findByIndexClassificationAndIndexNameAndFavorite(classificationName, indexName, favorite, pageable)
//          .map(indexInfoMapper::toDto);
//    } else if (classificationName != null && indexName != null) {
//      return indexInfoRepository.findByIndexClassificationAndIndexName(classificationName, indexName, pageable)
//          .map(indexInfoMapper::toDto);
//    } else if (classificationName != null) {
//      return indexInfoRepository.findByIndexClassification(classificationName, pageable)
//          .map(indexInfoMapper::toDto);
//    } else if (indexName != null) {
//      return indexInfoRepository.findByIndexName(indexName, pageable)
//          .map(indexInfoMapper::toDto);
//    } else if (favorite != null) {
//      return indexInfoRepository.findByFavorite(favorite, pageable)
//          .map(indexInfoMapper::toDto);
//    }
    return Page.empty();
  }

  @Override
  public IndexInfoDto updateIndexInfo(Long id, IndexInfoUpdateRequest updateDto) {
    // TODO: 추후 수정
    throw new UnsupportedOperationException("updateIndexInfo 추후 수정");
  }

  @Override
  public void deleteIndexInfo(Long id) {
    indexInfoRepository.deleteById(id);
  }
}
