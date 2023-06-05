package org.jetlinks.community.work.manager.service;

import lombok.AllArgsConstructor;
import org.hswebframework.ezorm.rdb.mapping.ReactiveRepository;
import org.jetlinks.community.reference.DataReferenceInfo;
import org.jetlinks.community.reference.DataReferenceManager;
import org.jetlinks.community.reference.DataReferenceProvider;
import org.jetlinks.community.work.manager.entity.WorkOrderEntity;
import org.jetlinks.community.work.manager.entity.WorkOrderRecordEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
public class WorkOrderReferenceProvider implements DataReferenceProvider {

    private final ReactiveRepository<WorkOrderRecordEntity, String> repository;

    @Override
    public String getId() {
        return DataReferenceManager.TYPE_DEVICE_GATEWAY;
    }

    @Override
    public Flux<DataReferenceInfo> getReference(String dataId) {
        return repository
            .createQuery()
            .where(WorkOrderRecordEntity::getWorkOrderId, dataId)
            .fetch()
            .map(e -> DataReferenceInfo
                .of(e.getWorkOrderId(),
                    DataReferenceManager.TYPE_DEVICE_GATEWAY,
                    e.getWorkOrderId(),
                    null));
    }

    @Override
    public Flux<DataReferenceInfo> getReferences() {
        return repository
            .createQuery()
            .fetch()
            .filter(e -> StringUtils.hasText(e.getId()))
            .map(e -> DataReferenceInfo
                .of(e.getWorkOrderId(),
                    DataReferenceManager.TYPE_DEVICE_GATEWAY,
                    e.getWorkOrderId(),
                    null));
    }
}
