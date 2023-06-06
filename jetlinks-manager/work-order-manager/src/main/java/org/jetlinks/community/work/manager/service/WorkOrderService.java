package org.jetlinks.community.work.manager.service;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.ezorm.rdb.mapping.ReactiveQuery;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.crud.events.*;
import org.hswebframework.web.crud.query.QueryHelper;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.jetlinks.community.reference.DataReferenceManager;
import org.jetlinks.community.work.manager.entity.WorkOrderDetail;
import org.jetlinks.community.work.manager.entity.WorkOrderDetailJoin;
import org.jetlinks.community.work.manager.entity.WorkOrderEntity;
import org.jetlinks.community.work.manager.entity.WorkOrderRecordEntity;
import org.jetlinks.rule.engine.api.RuleEngine;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WorkOrderService extends GenericReactiveCrudService<WorkOrderEntity, String> {

    private final WorkOrderRecordService workOrderRecordService;

    private final DataReferenceManager referenceManager;

    private final QueryHelper queryHelper;
    /**
     * 实体类新增操作前监听事件
     * @param event
     */
    @EventListener
    public void handleEvent(EntityBeforeCreateEvent<WorkOrderEntity> event) {
        event.async(
            handleEvent(event.getEntity())
        );
    }

    /**
     * 实体类更新操作前监听事件
     * @param event
     */
    @EventListener
    public void handleUpdateEvent(EntityModifyEvent<WorkOrderEntity> event) {
        event.async(
            handleUpdateEvent(event.getBefore())
        );
    }

    /**
     * 实体类删除操作前监听事件
     * @param event
     */
    @EventListener
    public void EntityBeforeDeleteEvent(EntityBeforeDeleteEvent<WorkOrderEntity> event) {
        event.async(
            Flux.fromIterable(event.getEntity())
                .flatMap(protocol -> referenceManager
                    .assertNotReferenced(DataReferenceManager.TYPE_DEVICE_GATEWAY, protocol.getId()))
        );
    }

    /**
     * 连表查询
     * @param query
     * @return
     */
    public Mono<PagerResult<WorkOrderDetail>> queryDetailPager(QueryParamEntity query) {
        return this
            .queryPager(query)
            .flatMap(result -> Flux
                .fromIterable(result.getData())
                .index()
                .flatMap(tp2 -> this
                    .convertDetail(tp2.getT2())
                    .map(detail -> Tuples.of(tp2.getT1(), detail)))
                .sort(Comparator.comparingLong(Tuple2::getT1))
                .map(Tuple2::getT2)
                .collectList()
                .map(detail -> PagerResult.of(result.getTotal(), detail, query)));
    }

    private Mono<WorkOrderDetail> convertDetail(WorkOrderEntity entity) {
        return workOrderRecordService
            .createQuery()
            .and(WorkOrderRecordEntity::getWorkOrderId,entity.getId())
            .fetch()
            .collectList()
            .map(workOrderRecordEntities -> WorkOrderDetail
                .of(entity)
                .withScene(workOrderRecordEntities));
    }

    public Mono<PagerResult<WorkOrderDetailJoin>> joinQueryWorkOrderDetail(QueryParamEntity query) {
        return queryHelper
            .select("select t.*,ext.* from work_order t" +
                    " left join work_order_record ext on ext.work_order_id = t.id",
                WorkOrderDetailJoin::new)
            //根据前端的动态条件参数自动构造查询条件以及分页排序等信息
            .where(query)
            .fetchPaged();
    }

    private Mono<Void> handleUpdateEvent(List<WorkOrderEntity> entity) {
        return Flux
            .fromIterable(entity)
            .flatMap(e -> workOrderRecordService
                .createQuery()
                .where(WorkOrderRecordEntity::getWorkOrderId, e.getId())
                .fetchOne()
                .flatMap(info -> {
                    info.setType("1");
                    return workOrderRecordService.updateById(info.getId(), info);
                }))
            .then();
    }

    private Mono<Void> handleEvent(List<WorkOrderEntity> entity) {
        return Flux
            .fromIterable(entity)
            .flatMap(e -> {
                if ("2".equals(e.getDescription())) {
                    return Mono.error(new Exception("内容不能为2"));
                }
                WorkOrderRecordEntity workOrderRecordEntity = new WorkOrderRecordEntity();
                workOrderRecordEntity.setType("0");
                workOrderRecordEntity.setWorkOrderId(e.getId());
                return workOrderRecordService.save(workOrderRecordEntity);
            })
            .then()
            .onErrorResume(err -> {
                log.error(err.getMessage(), err);
                return Mono.empty();
            });
    }

}
