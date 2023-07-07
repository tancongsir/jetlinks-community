package org.jetlinks.community.es.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.ezorm.rdb.mapping.annotation.DefaultValue;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.hswebframework.web.api.crud.entity.RecordCreationEntity;
import org.hswebframework.web.api.crud.entity.RecordModifierEntity;
import org.hswebframework.web.crud.generator.Generators;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Table;

@Table(name = "examine_es")
@Comment("es信息表")
@Setter
@Getter
public class ExamineESEntity extends GenericEntity<String>{

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(updatable = false)
    @Schema(description = "创建时间")
    private long timestamp;

}
