package org.jetlinks.community.work.manager.entity;

import com.microsoft.schemas.office.visio.x2012.main.TriggerType;
import lombok.Getter;
import lombok.Setter;
import org.hswebframework.web.bean.FastBeanCopier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class WorkOrderDetail {
    private String id;
    private String workOrderId;
    private String type;
    private String description;
    private Date createTime;
    private Date updateTime;
    private List<WorkOrderRecordEntity> workOrderRecordEntityList;

    public static WorkOrderDetail of(WorkOrderEntity entity) {
        return FastBeanCopier.copy(entity, new WorkOrderDetail(), "workOrderType");
    }

    public WorkOrderDetail withScene(List<WorkOrderRecordEntity> workOrderRecordEntities) {
        this.workOrderRecordEntityList = workOrderRecordEntities;
        return this;
    }
}
