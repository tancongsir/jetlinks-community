package org.jetlinks.community.work.manager.web;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.ezorm.rdb.mapping.ReactiveRepository;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.annotation.QueryAction;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.crud.service.ReactiveCrudService;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.gateway.annotation.Subscribe;
import org.jetlinks.community.gateway.external.Message;
import org.jetlinks.community.work.manager.entity.WorkOrderEntity;
import org.jetlinks.community.work.manager.service.WorkOrderService;
import org.jetlinks.community.work.manager.service.WorkOrderServiceImpl;
import org.jetlinks.core.event.EventBus;
import org.jetlinks.core.event.Subscription;
import org.jetlinks.core.message.DeviceMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RequestMapping("/work")
@RestController
@Authorize
@Resource(id = "work-order", name = "工单管理")
@Tag(name = "工单管理接口")
@AllArgsConstructor
public class WorkOrderController implements ReactiveServiceCrudController<WorkOrderEntity, String>, CommandLineRunner {

    private final WorkOrderService service;

    private final ReactiveRepository<WorkOrderEntity, String> workOrderEntityStringReactiveRepository;

    private final EventBus eventBus;

    @Override
    public ReactiveCrudService<WorkOrderEntity, String> getService() {
        return service;
    }

    @QueryAction
    @Operation(summary = "工单查询")
    @GetMapping("/word-order/_query")
    public Mono<PagerResult<WorkOrderEntity>> getWordOrder(@RequestBody Mono<QueryParamEntity> query) {
        return query.flatMap(service::queryPager);
    }


    @QueryAction
    @Operation(summary = "根据id删除工单")
    @DeleteMapping("/{id}/word-order/_delete")
    public Mono<WorkOrderEntity> deleteWordOrder(@PathVariable String id) {
        return delete(id);
    }

    @QueryAction
    @Operation(summary = "根据id修改工单")
    @DeleteMapping("/{id}/word-order/_update")
    public Mono<Void> updateWordOrder(@PathVariable String id) {
        return workOrderEntityStringReactiveRepository
            .createUpdate().where(WorkOrderEntity::getId, id).execute().then();
    }

    @QueryAction
    @Operation(summary = "新增工单")
    @DeleteMapping("/word-order/_add")
    public Mono<WorkOrderEntity> addWordOrder(@RequestBody Mono<WorkOrderEntity> payload) {
        return add(payload);
    }


    @Override
    public void run(String... args) {
        Flux.interval(Duration.ofMillis(10000)).flatMap(i -> {
            WorkOrderEntity workOrderEntity = new WorkOrderEntity();
            workOrderEntity.setType("2");
            workOrderEntity.setWorkOrderId("2");
            workOrderEntity.setDescription("2");
            return add(Mono.just(workOrderEntity))
                .then(eventBus.publish("/work/add",workOrderEntity));
        })
            .onErrorResume(err -> Mono.empty())
            .subscribe();
    }


}
