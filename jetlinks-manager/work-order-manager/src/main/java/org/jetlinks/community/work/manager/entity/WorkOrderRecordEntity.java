package org.jetlinks.community.work.manager.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;

import javax.persistence.Column;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "work_order_record")
@Comment("工单处理记录表")
@EnableEntityEvent
public class WorkOrderRecordEntity extends GenericEntity<String> {

    @Column(length = 64)
    @Schema(description = "状态")
    private String type;

    @Column(length = 64)
    @Schema(description = "工单id")
    private String workOrderId;
}
