package org.jetlinks.community.notify.wechat.corp.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;


@Setter
public class GetOneUserResponse extends ApiResponse{

    @JsonProperty
    @JsonAlias("userid")
    private String id;

    @JsonProperty
    private String name;
}
