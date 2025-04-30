package com.sprint.findex.sb02findexteam4.index.data.repository;

import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCsvExportCommand;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataSearchCondition;
import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import java.util.List;

public interface IndexDataRepositoryCustom {

  List<IndexData> findWithConditions(IndexDataSearchCondition command);

  Long countWithConditions(IndexDataSearchCondition command);

  List<IndexData> findAllForCsvExport(IndexDataCsvExportCommand command);
}
