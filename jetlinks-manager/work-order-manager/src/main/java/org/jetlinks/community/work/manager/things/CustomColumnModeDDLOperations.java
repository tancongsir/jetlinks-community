package org.jetlinks.community.work.manager.things;

import org.jetlinks.community.things.data.operations.DataSettings;
import org.jetlinks.community.things.data.operations.MetricBuilder;
import org.jetlinks.community.things.data.operations.RowModeDDLOperationsBase;
import org.jetlinks.core.metadata.PropertyMetadata;
import org.jetlinks.core.things.ThingsRegistry;
import reactor.core.publisher.Mono;

import java.util.List;

public class CustomColumnModeDDLOperations extends RowModeDDLOperationsBase {
    private final CustomHelper helper;

    public CustomColumnModeDDLOperations(String thingType,
                                         String templateId,
                                         String thingId,
                                         DataSettings settings,
                                         MetricBuilder metricBuilder,
                                         CustomHelper helper) {
        super(thingType, templateId, thingId, settings, metricBuilder);
        this.helper = helper;
    }

    @Override
    protected Mono<Void> register(MetricType metricType, String metric, List<PropertyMetadata> properties) {
        return Mono.empty();
    }

    @Override
    protected Mono<Void> reload(MetricType metricType, String metric, List<PropertyMetadata> properties) {
        return Mono.empty();
    }
}
