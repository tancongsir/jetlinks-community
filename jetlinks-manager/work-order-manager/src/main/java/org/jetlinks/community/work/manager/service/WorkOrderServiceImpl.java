package org.jetlinks.community.work.manager.service;

import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.jetlinks.community.work.manager.entity.WorkOrderEntity;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkOrderServiceImpl {

    Mono<PagerResult<WorkOrderEntity>> query(Mono<QueryParamEntity> query);

    Flux<WorkOrderEntity> add();

    Flux<WorkOrderEntity> update();

    Mono<Void> delete();
}
