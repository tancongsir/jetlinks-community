package org.jetlinks.community.work.manager.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.ezorm.rdb.mapping.ReactiveQuery;
import org.hswebframework.web.crud.events.*;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.jetlinks.community.reference.DataReferenceManager;
import org.jetlinks.community.work.manager.entity.WorkOrderEntity;
import org.jetlinks.community.work.manager.entity.WorkOrderRecordEntity;
import org.jetlinks.rule.engine.api.RuleEngine;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WorkOrderService extends GenericReactiveCrudService<WorkOrderEntity, String> {

    private final WorkOrderRecordService workOrderRecordService;

    private final DataReferenceManager referenceManager;

    @EventListener
    public void handleEvent(EntityBeforeCreateEvent<WorkOrderEntity> event) {
        event.async(
            handleEvent(event.getEntity())
        );
    }

    @EventListener
    public void handleUpdateEvent(EntityModifyEvent<WorkOrderEntity> event) {
        event.async(
            handleUpdateEvent(event.getBefore())
        );
    }

    @EventListener
    public void EntityBeforeDeleteEvent(EntityBeforeDeleteEvent<WorkOrderEntity> event) {
        event.async(
            Flux.fromIterable(event.getEntity())
                .flatMap(protocol -> referenceManager
                    .assertNotReferenced(DataReferenceManager.TYPE_DEVICE_GATEWAY, protocol.getId()))
        );
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
                if ("2".equals(e.getType())) {
                    return Mono.error(new Exception("类型不能为2"));
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
