package org.jetlinks.community.message;

import lombok.AllArgsConstructor;
import org.jetlinks.community.device.message.DeviceMessageConnector;
import org.jetlinks.core.device.DeviceOperator;
import org.jetlinks.core.device.DeviceRegistry;
import org.jetlinks.core.event.EventBus;
import org.jetlinks.core.message.DeviceMessage;
import org.jetlinks.core.message.interceptor.DeviceMessageSenderInterceptor;
import org.jetlinks.core.message.property.WritePropertyMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ExamineMessageInterceptor implements DeviceMessageSenderInterceptor {

    private final EventBus eventBus;

    private final DeviceRegistry registry;

    //examine-todo 第6题
    @Override
    public Mono<DeviceMessage> preSend(DeviceOperator device, DeviceMessage message) {
        Mono<Void> then = Mono.empty();
        WritePropertyMessage message1 = (WritePropertyMessage) message;
        message1.setDeviceId("7042926707040145006");
        return DeviceMessageConnector
            .createDeviceMessageTopic(registry, message)
            .flatMap(topic -> eventBus.publish(topic, message))
            .then(then).thenReturn(message1);
    }
}
