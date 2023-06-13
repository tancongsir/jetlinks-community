package org.jetlinks.community.notify.wechat.corp;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CorpTagUser {

    @JsonProperty
    @JsonAlias("name")
    private String name;

    @JsonProperty
    @JsonAlias("userid")
    private String userid;
}
