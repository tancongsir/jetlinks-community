package org.jetlinks.community.work.manager.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class WordOrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String description;


    private Date createTime;


    private Date updateTime;


    private String type;

//
//    private String wtype;
//
//    private String wid;

    private String id;

//    private String workOrderId;

    private List<WorkOrderRecordEntity> list;
}
