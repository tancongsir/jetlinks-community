package org.jetlinks.community.managing;

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
public class ExamineUserAddProvider implements SubscriptionProvider{

    private final EventBus eventBus;

    @Override
    public String id() {
        return "examineuser-add";
    }

    @Override
    public String name() {
        return "examineuser添加";
    }

    @Override
    public String[] getTopicPattern() {
        return new String[]{"/examineuser/add"};
    }

    @Override
    public Flux<?> subscribe(SubscribeRequest request) {
        return eventBus
            .subscribe(
                Subscription.of(
                    "ExamineUserAddProvider:" + request.getAuthentication().getUser().getId(),
                    new String[]{"/examineuser/add"},
                    Subscription.Feature.local,
                    Subscription.Feature.broker
                ))
            .map(topicMessage -> Message.success(request.getId(), topicMessage.getTopic(), topicMessage.decode()));
    }


}
