package org.jetlinks.community.work.manager.managing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.crud.service.ReactiveCrudService;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.gateway.annotation.Subscribe;
import org.jetlinks.community.gateway.external.Message;
import org.jetlinks.community.gateway.external.SubscribeRequest;
import org.jetlinks.community.gateway.external.SubscriptionProvider;
import org.jetlinks.community.work.manager.entity.WorkOrderEntity;
import org.jetlinks.community.work.manager.service.WorkOrderService;
import org.jetlinks.community.work.manager.web.WorkOrderController;
import org.jetlinks.core.event.EventBus;
import org.jetlinks.core.event.Subscription;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
@AllArgsConstructor
public class WorkOrderProvider implements SubscriptionProvider{

    private final EventBus eventBus;

    @Override
    public String id() {
        return "word-order-add";
    }

    @Override
    public String name() {
        return "工单添加";
    }

    @Override
    public String[] getTopicPattern() {
        return new String[]{"/work/order/add"};
    }

    @Override
    public Flux<?> subscribe(SubscribeRequest request) {
        return eventBus
            .subscribe(
                org.jetlinks.core.event.Subscription.of(
                    "DeviceMessageSubscriptionProvider:" + request.getAuthentication().getUser().getId(),
                    new String[]{"/work/add"},
                    org.jetlinks.core.event.Subscription.Feature.local,
                    Subscription.Feature.broker
                ))
            .map(topicMessage -> Message.success(request.getId(), topicMessage.getTopic(), topicMessage.decode()));
    }


}
