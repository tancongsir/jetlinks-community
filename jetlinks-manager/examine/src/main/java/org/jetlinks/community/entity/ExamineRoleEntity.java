package org.jetlinks.community.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.ezorm.rdb.mapping.annotation.DefaultValue;
import org.hswebframework.ezorm.rdb.mapping.annotation.EnumCodec;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;
import org.hswebframework.web.crud.generator.Generators;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Table(name = "examine_role")
@Comment("角色管理表")
@EnableEntityEvent
public class ExamineRoleEntity extends GenericEntity<String> {

    @Column(length = 64)
    @Length(min = 1, max = 64)
    @Schema(description = "名称")
    private String rolename;

    @Column
    @Length(max = 255)
    @Schema(description = "说明")
    private String description;

    @Column(length = 32)
    @Schema(description = "状态。1正常，0已禁用")
    private Integer state;

    @Column(name = "user_id",updatable = false)
    @Schema(description = "创建时间")
    @DefaultValue(generator = Generators.CURRENT_TIME)
    private String userId;
}
