package org.jetlinks.community.work.manager.configuration;

import org.jetlinks.community.things.data.ThingsDataRepositoryStrategy;
import org.jetlinks.community.work.manager.things.CustomHelper;
import org.jetlinks.community.work.manager.things.CustomRowModeStrategy;
import org.jetlinks.core.things.ThingsRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ThingsDataRepositoryStrategy.class)
public class CustomModeThingDataConfiguration {
    @Bean
    public CustomRowModeStrategy customRowModeThingDataPolicy(
        ThingsRegistry registry,
        CustomHelper customHelper) {
        return new CustomRowModeStrategy(registry, customHelper);
    }


}
