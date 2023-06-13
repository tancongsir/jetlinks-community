package org.jetlinks.community.notify.wechat.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.jetlinks.community.notify.DefaultNotifyType;
import org.jetlinks.community.notify.NotifierManager;
import org.jetlinks.community.notify.annotation.NotifierResource;
import org.jetlinks.community.notify.wechat.corp.CorpDepartment;
import org.jetlinks.community.notify.wechat.corp.CorpTag;
import org.jetlinks.community.notify.wechat.corp.CorpTagUser;
import org.jetlinks.community.notify.wechat.corp.CorpUser;
import org.jetlinks.community.notify.wechat.corp.request.*;
import org.jetlinks.community.notify.wechat.corp.response.*;
import org.jetlinks.core.command.CommandSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notifier/wechat/corp")
@Tag(name = "企业微信接口")
@AllArgsConstructor
@NotifierResource
public class WechatCoreNotifierController {

    private final NotifierManager notifierManager;

    @GetMapping("/{configId}/tags")
    @Operation(summary = "获取微信企业标签列表")
    public Flux<CorpTag> getTagList(@PathVariable String configId) {
        return notifierManager
            .getNotifier(DefaultNotifyType.weixin, configId)
            .map(notifier -> notifier.unwrap(CommandSupport.class))
            .flatMap(notifier -> notifier.execute(new GetTagRequest()))
            .flatMapIterable(GetTagResponse::getTagList);
    }

    @Operation(summary = "获取微信企业部门列表")
    @GetMapping("/{configId}/departments")
    public Flux<CorpDepartment> getDepartmentList(@PathVariable String configId) {
        return notifierManager
            .getNotifier(DefaultNotifyType.weixin, configId)
            .map(notifier -> notifier.unwrap(CommandSupport.class))
            .flatMap(notifier -> notifier.execute(new GetDepartmentRequest()))
            .flatMapIterable(GetDepartmentResponse::getDepartment);
    }

    @Operation(summary = "获取微信企业成员列表")
    @GetMapping("/{configId}/{departmentId}/users")
    public Flux<CorpUser> getCorpUserList(@PathVariable String configId,
                                          @PathVariable String departmentId) {
        return notifierManager
            .getNotifier(DefaultNotifyType.weixin, configId)
            .map(notifier -> notifier.unwrap(CommandSupport.class))
            .flatMap(notifier -> notifier.execute(new GetUserRequest(departmentId, false)))
            .flatMapIterable(GetUserResponse::getUserList);
    }

    @Operation(summary = "获取微信企业全部成员列表")
    @GetMapping("/{configId}/users")
    public Flux<CorpUser> getCorpAllUserList(@PathVariable String configId) {
        return notifierManager
            .getNotifier(DefaultNotifyType.weixin, configId)
            .map(notifier -> notifier.unwrap(CommandSupport.class))
            .flatMapMany(notifier -> notifier
                .execute(new GetDepartmentRequest())
                .flatMapIterable(GetDepartmentResponse::getDepartment)
                .flatMap(department -> notifier.execute(new GetUserRequest(department.getId(), false))))
            .flatMapIterable(GetUserResponse::getUserList);
    }

    @Operation(summary = "获取微信企业成员")
    @GetMapping("/{configId}/{userId}/user")
    public Mono<GetOneUserResponse> getCorpUser(@PathVariable String configId,
                                         @PathVariable String userId) {
        return notifierManager
            .getNotifier(DefaultNotifyType.weixin, configId)
            .map(notifier -> notifier.unwrap(CommandSupport.class))
            .flatMap(notifier -> notifier.execute(new GetOneUserRequest(userId)));
    }


    @Operation(summary = "获取部门详情")
    @GetMapping("/{configId}/{departmentId}/department")
    public Mono<CorpDepartment> getCorpDepartment(@PathVariable String configId,
                                                @PathVariable String departmentId) {
        return notifierManager
            .getNotifier(DefaultNotifyType.weixin, configId)
            .map(notifier -> notifier.unwrap(CommandSupport.class))
            .flatMap(notifier -> notifier.execute(new GetOneDepartmentRequest(departmentId)))
            .map(GetOneDepartmentResponse::getDepartment);
    }

    @Operation(summary = "获取标签详情")
    @GetMapping("/{configId}/{tagId}/tag")
    public Mono<GetOneTagUserResponse> getCorpTag(@PathVariable String configId,
                                        @PathVariable String tagId) {
        return notifierManager
            .getNotifier(DefaultNotifyType.weixin, configId)
            .map(notifier -> notifier.unwrap(CommandSupport.class))
            .flatMap(notifier -> notifier.execute(new GetOneTagUserRequest(tagId)));
    }
}
