package org.jetlinks.community.work.manager.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.ezorm.rdb.mapping.annotation.DefaultValue;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;
import org.hswebframework.web.crud.generator.Generators;
import org.hswebframework.web.exception.BusinessException;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Table(name = "work_order")
@Comment("工单管理表")
@EnableEntityEvent
public class WorkOrderEntity extends GenericEntity<String> {

    @Column
    @Schema(description = "说明")
    private String description;

    @Column
    @Schema(description = "创建时间")
    @DefaultValue(generator = Generators.CURRENT_TIME)
    private Date createTime;

    @Column
    @Schema(description = "更新时间")
    @DefaultValue(generator = Generators.CURRENT_TIME)
    private Date updateTime;

}
