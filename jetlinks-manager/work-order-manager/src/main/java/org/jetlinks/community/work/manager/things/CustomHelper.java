package org.jetlinks.community.work.manager.things;

import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;

import org.jetlinks.community.timeseries.TimeSeriesData;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;

@Component
public class CustomHelper{

    public HashMap<String,HashMap<String,List<TimeSeriesData>>> properties = new HashMap<>();

    public void save(String metric,TimeSeriesData data){
        List<TimeSeriesData> objects = new ArrayList<>();
        if (properties.isEmpty() || properties.get(metric).isEmpty()){
            HashMap<String,List<TimeSeriesData>> map = new HashMap<>();
            objects.add(data);
            if (data.get("property").isPresent()){
                map.put(String.valueOf(data.get("property").get()),objects);
                properties.put(metric,map);
            }
        }else {
            for (String s : properties.keySet()) {
                if (metric.equals(s)){
                    for (String s1 : properties.get(s).keySet()) {
                        if (data.get("property").isPresent()){
                            if (data.get("property").get().equals(s1)){
                                properties.get(s).get(s1).add(data);
                                break;
                            }
                            objects.add(data);
                            properties.get(s).put((String) data.get("property").get(),objects);
                            break;
                        }
                    }
                }
            }
        }
    }

    public <T> Mono<PagerResult<T>> doQueryPager(String metric, QueryParamEntity param, Function<TimeSeriesData, T> mapper) {
        List<T> objects = new ArrayList<>();
        if (!properties.isEmpty()){
            HashMap<String,List<TimeSeriesData>>  payload =  this.properties.get(metric);
            for (String s : payload.keySet()) {
                if (s.equals(param.getTerms().get(1).getValue().toString().replace("[","").replace("]",""))){
                    for (TimeSeriesData timeSeriesDatum : payload.get(s)) {
                        objects.add(mapper.apply(timeSeriesDatum));
                    }
                }
            }
        }
        return Mono.just(PagerResult.of(0,objects,param));
    }
}
