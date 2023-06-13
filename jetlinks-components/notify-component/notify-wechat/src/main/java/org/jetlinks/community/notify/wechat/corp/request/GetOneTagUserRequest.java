package org.jetlinks.community.notify.wechat.corp.request;


import lombok.AllArgsConstructor;
import org.jetlinks.community.notify.wechat.corp.response.ApiResponse;
import org.jetlinks.community.notify.wechat.corp.response.GetOneTagUserResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GetOneTagUserRequest extends ApiRequest<GetOneTagUserResponse> {

    private final String tagId;

    @Override
    public Mono<GetOneTagUserResponse> execute(WebClient client) {
        return client
            .post()
            .uri("/cgi-bin/tag/get", builder -> builder
                .queryParam("tagid",tagId)
                .build())
            .retrieve()
            .bodyToMono(GetOneTagUserResponse.class)
            .doOnNext(ApiResponse::assertSuccess);
    }
}
