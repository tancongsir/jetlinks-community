package org.jetlinks.community.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.ezorm.rdb.mapping.annotation.DefaultValue;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;
import org.hswebframework.web.crud.generator.Generators;
import org.hswebframework.web.validator.CreateGroup;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@Table(name = "examine_user")
@Comment("用户管理表")
@EnableEntityEvent
public class ExamineUserEntity extends GenericEntity<String> {
    @Column(nullable = false)
    @NotBlank
    @Schema(description = "密码")
    private String password;

    @Column(nullable = false)
    @NotBlank
    @Schema(description = "加密盐值")
    private String salt;

    @Column(updatable = false)
    @Schema(description = "创建时间")
    @DefaultValue(generator = Generators.CURRENT_TIME)
    private Date createTime;

    @Column
    @Schema(description = "创建者ID")
    private String creatorId;

    @Column(length = 128, nullable = false)
    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空", groups = CreateGroup.class)
    private String name;

    @Column
    @Schema(description = "用户类型")
    private String type;

    @Column
    @Schema(description = "用户状态")
    private Integer status;

    @Column(length = 128, nullable = false)
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空", groups = CreateGroup.class)
    private String username;
}
