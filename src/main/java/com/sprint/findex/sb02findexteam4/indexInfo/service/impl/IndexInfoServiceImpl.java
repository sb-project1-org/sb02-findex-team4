package com.sprint.findex.sb02findexteam4.indexInfo.service.impl;

import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoCreateRequestDto;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoUpdateRequestDto;
import com.sprint.findex.sb02findexteam4.indexInfo.entity.IndexInfo;
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
  public void registerIndexInfo(IndexInfoCreateRequestDto requestDto) {
    indexInfoValidator.validateForCreate(requestDto);
    IndexInfo indexInfo = IndexInfo.create(requestDto);
    indexInfoRepository.save(indexInfo);
  }

  @Override
  public Page<IndexInfoDto> getIndexInfoWithFilters(String classificationName, String indexName,
      Boolean favorite, Pageable pageable) {
    if (classificationName != null && indexName != null && favorite != null) {
      return indexInfoRepository.findByTypeAndNameAndFavorite(classificationName, indexName, favorite, pageable)
          .map(indexInfoMapper::toDto);
    } else if (classificationName != null && indexName != null) {
      return indexInfoRepository.findByTypeAndName(classificationName, indexName, pageable)
          .map(indexInfoMapper::toDto);
    } else if (classificationName != null) {
      return indexInfoRepository.findByClassificationName(classificationName, pageable)
          .map(indexInfoMapper::toDto);
    } else if (indexName != null) {
      return indexInfoRepository.findByIndexName(indexName, pageable)
          .map(indexInfoMapper::toDto);
    } else if (favorite != null) {
      return indexInfoRepository.findByFavorite(favorite, pageable)
          .map(indexInfoMapper::toDto);
    }
    return Page.empty();
  }

  @Override
  public void updateIndexInfo(Long id, IndexInfoUpdateRequestDto updateDto) {
    // TODO: 추후 수정
    throw new UnsupportedOperationException("updateIndexInfo 메서드는 아직 구현되지 않았습니다.");
  }

  @Override
  public void deleteIndexInfo(Long id) {
    indexInfoRepository.deleteById(id);
  }
}
