package org.jetlinks.community.work.manager.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.ezorm.rdb.mapping.ReactiveRepository;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.annotation.QueryAction;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.crud.service.ReactiveCrudService;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.work.manager.entity.WorkOrderEntity;
import org.jetlinks.community.work.manager.service.WorkOrderService;
import org.jetlinks.community.work.manager.service.WorkOrderServiceImpl;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/work")
@RestController
@Authorize
@Resource(id = "work-order", name = "工单管理")
@Tag(name = "工单管理接口")
@AllArgsConstructor
public class WorkOrderController implements ReactiveServiceCrudController<WorkOrderEntity,String> {

    private final WorkOrderService service;

    private final ReactiveRepository<WorkOrderEntity, String> workOrderEntityStringReactiveRepository;
    @Override
    public ReactiveCrudService<WorkOrderEntity, String> getService() {
        return service;
    }

    @QueryAction
    @Operation(summary = "工单查询")
    @GetMapping("/word-order/_query")
    public Mono<PagerResult<WorkOrderEntity>> getWordOrder(@RequestBody Mono<QueryParamEntity> query){
        return query.flatMap(service::queryPager);
    }


    @QueryAction
    @Operation(summary = "根据id删除工单")
    @DeleteMapping("/{id}/word-order/_delete")
    public Mono<Void> deleteWordOrder(@PathVariable String id){
        return workOrderEntityStringReactiveRepository
            .createDelete().where(WorkOrderEntity::getId,id).execute().then();
    }

    @QueryAction
    @Operation(summary = "根据id修改工单")
    @DeleteMapping("/{id}/word-order/_update")
    public Mono<Void> updateWordOrder(@PathVariable String id){
        return workOrderEntityStringReactiveRepository
            .createUpdate().where(WorkOrderEntity::getId,id).execute().then();
    }

    @QueryAction
    @Operation(summary = "新增工单")
    @DeleteMapping("/word-order/_add")
    public Mono<WorkOrderEntity> addWordOrder(@RequestBody Mono<WorkOrderEntity> payload){
        return add(payload);
    }
}
