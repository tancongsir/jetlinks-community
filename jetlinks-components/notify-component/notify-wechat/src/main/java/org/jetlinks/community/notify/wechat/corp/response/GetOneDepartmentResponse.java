package org.jetlinks.community.notify.wechat.corp.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.jetlinks.community.notify.wechat.corp.CorpDepartment;

import java.util.Collections;
import java.util.List;


@Setter
public class GetOneDepartmentResponse extends ApiResponse{

    @JsonProperty
    @JsonAlias("department")
    private CorpDepartment department;

    public CorpDepartment getDepartment() {
        return department == null ? new CorpDepartment() : department;
    }

}
