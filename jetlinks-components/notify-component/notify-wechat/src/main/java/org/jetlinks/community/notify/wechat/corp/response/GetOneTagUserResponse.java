package org.jetlinks.community.notify.wechat.corp.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.jetlinks.community.notify.wechat.corp.CorpDepartment;
import org.jetlinks.community.notify.wechat.corp.CorpTag;
import org.jetlinks.community.notify.wechat.corp.CorpTagUser;
import org.jetlinks.community.notify.wechat.corp.CorpUser;

import java.util.Collections;
import java.util.List;


@Setter
public class GetOneTagUserResponse extends ApiResponse{

    @JsonProperty
    @JsonAlias("tagname")
    private String tagName;

    @JsonProperty
    @JsonAlias("userlist")
    private List<CorpTagUser> corpTagUser;


}
