package org.jetlinks.community.work.manager.web.excel;

import lombok.Getter;
import lombok.Setter;
import org.hswebframework.reactor.excel.CellDataType;
import org.hswebframework.reactor.excel.ExcelHeader;
import org.hswebframework.web.bean.FastBeanCopier;
import org.jetlinks.community.work.manager.entity.WorkOrderEntity;
import org.jetlinks.core.metadata.ConfigPropertyMetadata;
import org.jetlinks.core.metadata.PropertyMetadata;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.*;

@Getter
@Setter
public class WorkOrderExcelInfo {

    @NotBlank(message = "id不能为空")
    private String id;

    @NotBlank(message = "工单id不能为空")
    private String workOrderId;

    private String description;

    private String type;

    private List<WorkOrderEntity> tags = new ArrayList<>();

    private long rowNumber;

    public Map<String,Object> toMap(){
        return FastBeanCopier.copy(this,new HashMap<>());
    }


    public void with(String key, Object value) {
        FastBeanCopier.copy(Collections.singletonMap(key, value), this);
    }

    public static List<ExcelHeader> getExportHeaderMapping(List<WorkOrderEntity> tags) {
        return new ArrayList<>(Arrays.asList(
            new ExcelHeader("id", "ID", CellDataType.STRING),
            new ExcelHeader("workOrderId", "工单编号", CellDataType.STRING),
            new ExcelHeader("description", "说明", CellDataType.STRING),
            new ExcelHeader("type", "工单类型", CellDataType.STRING)
        ));
    }

    public static Map<String, String> getImportHeaderMapping() {
        Map<String, String> mapping = new HashMap<>();

        mapping.put("ID", "id");
        mapping.put("说明", "description");
        mapping.put("工单编号", "workOrderId");
        mapping.put("工单类型", "type");

        return mapping;
    }
}
