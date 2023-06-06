package org.jetlinks.community.work.manager.entity;

import lombok.Getter;
import lombok.Setter;
import org.hswebframework.web.bean.FastBeanCopier;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class WorkOrderDetailJoin {
    private String id;
    private String description;
    private WorkOrderRecordEntity ext;
}
