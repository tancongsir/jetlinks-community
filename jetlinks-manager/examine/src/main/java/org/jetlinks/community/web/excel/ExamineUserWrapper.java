package org.jetlinks.community.web.excel;

import org.hswebframework.reactor.excel.Cell;
import org.hswebframework.reactor.excel.converter.RowWrapper;
import org.jetlinks.community.entity.ExamineUserEntity;

import java.util.HashMap;
import java.util.Map;

public class ExamineUserWrapper extends RowWrapper<ExamineUserExcelInfo> {
    Map<String, ExamineUserEntity> tagMapping = new HashMap<>();
    static Map<String, String> headerMapping = ExamineUserExcelInfo.getImportHeaderMapping();

    @Override
    protected ExamineUserExcelInfo newInstance() {
        return new ExamineUserExcelInfo();
    }


    @Override
    protected ExamineUserExcelInfo wrap(ExamineUserExcelInfo workOrderExcelInfo, Cell header, Cell cell) {
        String headerText = header.valueAsText().orElse("null");
        workOrderExcelInfo.with(headerMapping.getOrDefault(headerText, headerText), cell.value().orElse(null));
        workOrderExcelInfo.setRowNumber(cell.getRowIndex());
        return workOrderExcelInfo;
    }
}
