package org.jetlinks.community.notify.wechat.corp.request;


import lombok.AllArgsConstructor;
import org.jetlinks.community.notify.wechat.corp.response.ApiResponse;
import org.jetlinks.community.notify.wechat.corp.response.GetOneUserResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GetOneUserRequest extends ApiRequest<GetOneUserResponse> {

    private final String userid;

    @Override
    public Mono<GetOneUserResponse> execute(WebClient client) {
        return client
            .post()
            .uri("/cgi-bin/user/get", builder -> builder
                .queryParam("userid",userid)
                .build())
            .retrieve()
            .bodyToMono(GetOneUserResponse.class)
            .doOnNext(ApiResponse::assertSuccess);
    }
}
