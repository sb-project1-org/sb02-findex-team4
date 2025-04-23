package com.sprint.findex.sb02findexteam4.indexInfo.service.impl;

import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoCreateRequestDto;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoUpdateRequestDto;
import com.sprint.findex.sb02findexteam4.indexInfo.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.indexInfo.repository.IndexInfoRepository;
import com.sprint.findex.sb02findexteam4.indexInfo.service.IndexInfoService;
import com.sprint.findex.sb02findexteam4.indexInfo.service.IndexInfoValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IndexInfoServiceImpl implements IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexInfoValidator indexInfoValidator;

  public IndexInfoServiceImpl(IndexInfoRepository indexInfoRepository, IndexInfoValidator indexInfoValidator) {
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
    return indexInfoRepository.findByTypeAndNameAndFavorite(classificationName, indexName, favorite, pageable)
        .map(indexInfo -> new IndexInfoDto(
            indexInfo.getId(),
            indexInfo.getIndexClassification(),
            indexInfo.getIndexName(),
            indexInfo.getEmployedItemsCount(),
            indexInfo.getBasePointInTime(),
            indexInfo.getBaseIndex(),
            indexInfo.getSourceType(),
            indexInfo.getFavorite()
        ));

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
