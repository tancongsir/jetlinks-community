package org.jetlinks.community.work.manager.service;

import lombok.AllArgsConstructor;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.jetlinks.community.work.manager.entity.WorkOrderEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WorkOrderService extends GenericReactiveCrudService<WorkOrderEntity, String> {


}
