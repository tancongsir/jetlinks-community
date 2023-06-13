package org.jetlinks.community.notify.wechat.corp.request;


import lombok.AllArgsConstructor;
import org.jetlinks.community.notify.wechat.corp.response.ApiResponse;
import org.jetlinks.community.notify.wechat.corp.response.GetOneDepartmentResponse;
import org.jetlinks.community.notify.wechat.corp.response.GetOneUserResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GetOneDepartmentRequest extends ApiRequest<GetOneDepartmentResponse> {

    private final String departmentId;

    @Override
    public Mono<GetOneDepartmentResponse> execute(WebClient client) {
        return client
            .post()
            .uri("/cgi-bin/department/get", builder -> builder
                .queryParam("id",departmentId)
                .build())
            .retrieve()
            .bodyToMono(GetOneDepartmentResponse.class)
            .doOnNext(ApiResponse::assertSuccess);
    }
}
