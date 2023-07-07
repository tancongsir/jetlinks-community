package org.jetlinks.community.entity;

import lombok.Getter;
import lombok.Setter;
import org.jetlinks.community.device.entity.DeviceInstanceEntity;

import java.util.List;

@Getter
@Setter
public class ExamineTwoDataBaseEntity {

    private List<ExamineUserEntity> entity;

    private List<DeviceInstanceEntity> deviceInstanceEntity;


}
