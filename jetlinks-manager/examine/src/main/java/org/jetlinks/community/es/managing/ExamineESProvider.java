package org.jetlinks.community.es.managing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetlinks.community.gateway.external.Message;
import org.jetlinks.community.gateway.external.SubscribeRequest;
import org.jetlinks.community.gateway.external.SubscriptionProvider;
import org.jetlinks.core.event.EventBus;
import org.jetlinks.core.event.Subscription;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@AllArgsConstructor
public class ExamineESProvider implements SubscriptionProvider{

    private final EventBus eventBus;

    @Override
    public String id() {
        return "es-query";
    }

    @Override
    public String name() {
        return "es查询";
    }

    @Override
    public String[] getTopicPattern() {
        return new String[]{"/es/query"};
    }

    @Override
    public Flux<?> subscribe(SubscribeRequest request) {
        return eventBus
            .subscribe(
                Subscription.of(
                    "ExamineESProvider:" + request.getAuthentication().getUser().getId(),
                    new String[]{"/es/query"},
                    Subscription.Feature.local,
                    Subscription.Feature.broker
                ))
            .map(topicMessage -> Message.success(request.getId(), topicMessage.getTopic(), topicMessage.decode()));
    }


}
