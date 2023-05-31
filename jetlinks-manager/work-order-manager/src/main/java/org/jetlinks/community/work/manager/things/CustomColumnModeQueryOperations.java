package org.jetlinks.community.work.manager.things;

import org.hswebframework.ezorm.core.dsl.Query;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.jetlinks.community.things.data.AggregationRequest;
import org.jetlinks.community.things.data.ThingPropertyDetail;
import org.jetlinks.community.things.data.operations.DataSettings;
import org.jetlinks.community.things.data.operations.MetricBuilder;
import org.jetlinks.community.things.data.operations.RowModeQueryOperationsBase;
import org.jetlinks.community.timeseries.TimeSeriesData;
import org.jetlinks.community.timeseries.query.AggregationData;
import org.jetlinks.core.things.ThingsRegistry;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class CustomColumnModeQueryOperations extends RowModeQueryOperationsBase {
    private final CustomHelper helper;
    public CustomColumnModeQueryOperations(String thingType,
                                           String thingTemplateId,
                                           String thingId,
                                           MetricBuilder metricBuilder,
                                           DataSettings settings,
                                           ThingsRegistry registry,
                                           CustomHelper helper) {
        super(thingType, thingTemplateId, thingId, metricBuilder, settings, registry);
        this.helper = helper;
    }


    @Override
    protected Flux<TimeSeriesData> doQuery(String metric, Query<?, QueryParamEntity> query) {
        return Flux.just();
    }

    @Override
    protected <T> Mono<PagerResult<T>> doQueryPage(String metric, Query<?, QueryParamEntity> query, Function<TimeSeriesData, T> mapper) {

        return helper.doQueryPager(metric, query.getParam(), mapper);
//        return Mono.empty();
    }


    @Override
    protected Flux<AggregationData> doAggregation(String metric, AggregationRequest request, AggregationContext context) {

        return Flux.empty();
    }
}
