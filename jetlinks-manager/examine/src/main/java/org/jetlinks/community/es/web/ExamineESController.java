package org.jetlinks.community.es.web;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.annotation.QueryAction;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.bean.FastBeanCopier;
import org.hswebframework.web.crud.service.ReactiveCrudService;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.elastic.search.service.ElasticSearchService;
import org.jetlinks.community.es.ExamineESEnum.ExamineIndexEnum;
import org.jetlinks.community.es.entity.ExamineESEntity;
import org.jetlinks.community.es.service.ExamineEsService;
import org.jetlinks.core.event.EventBus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/es")
@AllArgsConstructor
@Resource(id = "demo", name = "自定义属性数据存取接口")
@Tag(name = "自定义属性数据存取接口")
public class ExamineESController implements ReactiveServiceCrudController<ExamineESEntity, String> {

    //引入service
    private final ElasticSearchService elasticSearchService;

    private final ExamineEsService service;

    private final EventBus eventBus;

    @Override
    public ReactiveCrudService<ExamineESEntity, String> getService() {
        return service;
    }


    @PostMapping("/save")
    @QueryAction
    public Mono<List<ExamineESEntity>> saveData(@RequestBody @NonNull List<ExamineESEntity> entity) {
        //设置保存时时间戳信息
        for (ExamineESEntity examineESEntity : entity) {
            examineESEntity.setTimestamp(System.currentTimeMillis());
        }
        return elasticSearchService.save(ExamineIndexEnum.custom.getIndex(), entity)
            .thenReturn(entity);

    }



    @PostMapping("/query/{_index}")
    @QueryAction
    public Mono<PagerResult<ExamineESEntity>> queryDataByIndex(@PathVariable String _index, QueryParamEntity queryParam) {
        return elasticSearchService.queryPager(_index, queryParam,map -> FastBeanCopier.copy(map,new ExamineESEntity()));
    }


    @PostMapping("/event/query/{_index}")
    @QueryAction
    public Mono<Long> queryDataByIndex2(@PathVariable String _index, QueryParamEntity queryParam) {
        return eventBus.publish("/es/query",elasticSearchService
            .queryPager(_index, queryParam, map -> FastBeanCopier.copy(map,new ExamineESEntity())));
    }
}