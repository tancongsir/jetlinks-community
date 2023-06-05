package org.jetlinks.community.work.manager.web.excel;

import org.hswebframework.reactor.excel.Cell;
import org.hswebframework.reactor.excel.converter.RowWrapper;
import org.hswebframework.web.bean.FastBeanCopier;
import org.jetlinks.community.work.manager.entity.WorkOrderEntity;
import org.jetlinks.core.metadata.PropertyMetadata;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WorkOrderWrapper extends RowWrapper<WorkOrderExcelInfo> {
    Map<String, WorkOrderEntity> tagMapping = new HashMap<>();
    static Map<String, String> headerMapping = WorkOrderExcelInfo.getImportHeaderMapping();

    @Override
    protected WorkOrderExcelInfo newInstance() {
        return new WorkOrderExcelInfo();
    }


    @Override
    protected WorkOrderExcelInfo wrap(WorkOrderExcelInfo workOrderExcelInfo, Cell header, Cell cell) {
        String headerText = header.valueAsText().orElse("null");
        workOrderExcelInfo.with(headerMapping.getOrDefault(headerText, headerText), cell.value().orElse(null));
        workOrderExcelInfo.setRowNumber(cell.getRowIndex());
        return workOrderExcelInfo;
    }
}
