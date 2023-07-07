package org.jetlinks.community.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExamineUserRoleInfo {

    private String id;

    private String password;

    private String salt;

    private String name;

    private String type;

    private Integer status;

    private String username;

    private String creatorId;

    private Date createTime;

    private String rolename;

    private String description;

    private Integer state;

    private String userId;
}
