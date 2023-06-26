package org.jetlinks.community.work.manager.web;

import io.grpc.Contexts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.ezorm.rdb.mapping.ReactiveRepository;
import org.hswebframework.reactor.excel.ReactorExcel;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.Dimension;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.annotation.QueryAction;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.bean.FastBeanCopier;
import org.hswebframework.web.crud.query.QueryHelper;
import org.hswebframework.web.crud.service.ReactiveCrudService;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.hswebframework.web.exception.BusinessException;
import org.jetlinks.community.io.excel.DefaultImportExportService;
import org.jetlinks.community.io.excel.ImportExportService;
import org.jetlinks.community.reference.DataReferenceManager;
import org.jetlinks.community.work.manager.entity.*;
import org.jetlinks.community.work.manager.response.ImportWorkOrderResult;
import org.jetlinks.community.work.manager.service.WorkOrderRecordService;
import org.jetlinks.community.work.manager.service.WorkOrderService;
import org.jetlinks.community.work.manager.web.excel.WorkOrderExcelInfo;
import org.jetlinks.community.work.manager.web.excel.WorkOrderWrapper;
import org.jetlinks.core.event.EventBus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import sun.plugin2.gluegen.runtime.BufferFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


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

    private final WorkOrderRecordService workOrderRecordService;

    private final DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

    private final ImportExportService importExportService;

    private final QueryHelper queryHelper;

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
    @PostMapping("/{id}/word-order/_update")
    public Mono<Void> updateWordOrder(@PathVariable String id, @RequestBody WorkOrderEntity workOrderEntity) {
        return workOrderEntityStringReactiveRepository
            .createUpdate()
            .set(workOrderEntity)
            .where(WorkOrderEntity::getId, id)
            .execute()
            .then();
    }

    @QueryAction
    @Operation(summary = "新增工单")
    @PostMapping("/word-order/_add")
    public Mono<WorkOrderEntity> addWordOrder(@RequestBody Mono<WorkOrderEntity> payload) {
        return add(payload);
    }

    @QueryAction
    @Operation(summary = "查询工单详情")
    @GetMapping("/word-order/detail/_query")
    public Mono<PagerResult<WorkOrderDetailJoin>> queryDetailWordOrder(@Parameter(hidden = true) QueryParamEntity query) {
//        return service.queryDetailPager(query);
        return service.joinQueryWorkOrderDetail(query);
    }

    @QueryAction
    @Operation(summary = "导出")
    @GetMapping("/excel.{format}")
    public Mono<Void> wordOrderExcel(ServerHttpResponse response,
                                     @Parameter(hidden = true) QueryParamEntity parameter,
                                     @PathVariable @Parameter(description = "文件格式,支持csv,xlsx") String format) throws IOException {
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=".concat(URLEncoder.encode("工单数据." + format, StandardCharsets.UTF_8
                .displayName())));
        return ReactorExcel.<WorkOrderExcelInfo>writer(format)
            .headers(WorkOrderExcelInfo.getExportHeaderMapping(Collections.emptyList()))
            .converter(WorkOrderExcelInfo::toMap)
            .writeBuffer(service
                .query(parameter)
                .map(entity -> FastBeanCopier.copy(entity, new WorkOrderExcelInfo(), "state")), 512 * 1024)
            .doOnError(err -> log.error(err.getMessage(), err))
            .map(bufferFactory::wrap)
            .as(response::writeWith);
    }

    @QueryAction
    @Operation(summary = "导入")
    @GetMapping(value = "/excel/output", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ImportWorkOrderResult> wordOrderOutput(@RequestParam(required = false) @Parameter(description = "文件地址,支持csv,xlsx文件格式") String fileUrl,
                                                       @RequestParam(required = false) @Parameter(description = "文件Id") String fileId) {
        return Authentication
            .currentReactive()
            .flatMapMany(auth -> {
                //从当前用户的维度中获取机构信息,需要将用户绑定到对应到机构.
                Map<String, String> orgMapping = auth
                    .getDimensions("org")
                    .stream()
                    .collect(Collectors.toMap(Dimension::getName, Dimension::getId, (_1, _2) -> _1));

                return importExportService
                    .readData(fileUrl, fileId, new WorkOrderWrapper())
                    .onErrorResume(err -> {
                        log.error(err.getMessage(), err);
                        return Mono.empty();
                    })
                    .map(info -> FastBeanCopier.copy(info, new WorkOrderEntity()))
                    .buffer(100)
                    .publishOn(Schedulers.single())
                    .concatMap(buffer ->
                        service.save(Flux.fromIterable(buffer)))
                            .map(ImportWorkOrderResult::success)
                            .onErrorResume(err -> Mono.just(ImportWorkOrderResult.error(err)));
            });
    }
    @GetMapping("/word-order/detail/_query/1")
    @QueryAction
    public Flux<WorkOrderRecordEntity> find() {
        return service
            .createQuery()
            .fetch()
            .flatMap(list -> {
                List<String> ids = new ArrayList<>();
                ids.add(list.getId());
                return workOrderRecordService
                    .createQuery()
                    .in(WorkOrderRecordEntity::getWorkOrderId,ids)
                    .fetch();
            });
    }

    @GetMapping("/word-order/detail/_query/2")
    @QueryAction
    public Mono<PagerResult<WordOrderInfo>> find2(QueryParamEntity query) {
        return QueryHelper.transformPageResult(
            service.queryPager(query),
            list -> QueryHelper
                .combineOneToMany(
                    Flux.fromIterable(list).map(e -> e.copyTo(new WordOrderInfo())),
                    WordOrderInfo::getId,
                    workOrderRecordService.createQuery(),
                    WorkOrderRecordEntity::getWorkOrderId,
                    WordOrderInfo::setList
                )
            .collectList());
    }

    @GetMapping("/word-order/detail/_query/3")
    @QueryAction
    public Mono<PagerResult<WordOrderInfo>> find3(QueryParamEntity query) {
        return queryHelper.select("select t.*,w.work_order_id as workOrderId,w.type as wtype,w.id as wid from work_order t left join work_order_record w on t.id = w.work_order_id", WordOrderInfo::new)
            .where(query)
            .fetchPaged();
    }

    @Override
    public void run(String... args) {
//        Flux.interval(Duration.ofMillis(10000)).flatMap(i -> {
//            WorkOrderEntity workOrderEntity = new WorkOrderEntity();
//            workOrderEntity.setType("2");
//            workOrderEntity.setWorkOrderId("2");
//            workOrderEntity.setDescription("2");
//            return add(Mono.just(workOrderEntity))
//                .then(eventBus.publish("/work/add",workOrderEntity));
//        })
//            .onErrorResume(err -> Mono.empty())
//            .subscribe();
    }


}
