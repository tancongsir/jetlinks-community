package org.jetlinks.community.servive;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.exception.AccessDenyException;
import org.hswebframework.web.crud.query.QueryHelper;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.hswebframework.web.exception.BusinessException;
import org.hswebframework.web.exception.NotFoundException;
import org.jetlinks.community.device.entity.DeviceInstanceEntity;
import org.jetlinks.community.entity.ExamineRoleEntity;
import org.jetlinks.community.entity.ExamineTwoDataBaseEntity;
import org.jetlinks.community.entity.ExamineUserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Service
@AllArgsConstructor
public class ExamineUserService extends GenericReactiveCrudService<ExamineUserEntity, String> {



    private final QueryHelper queryHelper;

    public Mono<PagerResult<ExamineUserEntity>> joinQueryExamineUserEntity(QueryParamEntity query) {
        return queryHelper
            .select("select eu.id,eu.name,eu.password,eu.salt,eu.status,eu.type,eu.username,er.rolename,er.user_id "+
                    "from examine_user eu left join examine_role er on eu.id = er.user_id GROUP BY eu.id,eu.name,"+
                    "eu.password,eu.salt,eu.status,eu.type,eu.username,er.rolename,er.user_id",
                ExamineUserEntity::new)
            //根据前端的动态条件参数自动构造查询条件以及分页排序等信息
            .where(query)
            .fetchPaged();
    }


    //examine-todo 第12题
    public Mono<PagerResult<ExamineUserEntity>> Examine12(QueryParamEntity query) {

        return queryHelper //函数1
            .select("select eu.id,eu.name,eu.password,eu.salt,eu.status,eu.type,eu.username,er.rolename,er.user_id "+
                    "from examine_user eu left join examine_role er on eu.id = er.user_id GROUP BY eu.id,eu.name,"+
                    "eu.password,eu.salt,eu.status,eu.type,eu.username,er.rolename,er.user_id",
                ExamineUserEntity::new)
            .where(query)
            .fetchPaged()
            .then(queryHelper //函数2
                .select("select * from examine_user",
                    ExamineUserEntity::new)
                .where(query)
                .fetchPaged()
                .switchIfEmpty(Mono.error(NotFoundException::new))//抛出异常
                .flatMap(info -> Mono.just(PagerResult.of(info.getData().size(),info.getData()))) //函数3
            );
    }

    public Flux<ExamineUserEntity> getExamineUser() {
        return createQuery().fetch();
    }

    public Flux<ExamineTwoDataBaseEntity> findAll() {

        Connection com2 = null;
        Statement stat2 = null;
        ResultSet rs2 = null;

        ArrayList<ExamineTwoDataBaseEntity> list = new ArrayList<>();
        ExamineTwoDataBaseEntity examineTwoDataBaseEntity = new ExamineTwoDataBaseEntity();
        try {
            Class.forName("org.postgresql.Driver");
            com2 = DriverManager.getConnection("jdbc:postgresql://192.168.33.237:5432/jetlinks", "postgres", "jetlinks");
            stat2 = com2.createStatement();
            String sql2 = "SELECT * FROM dev_device_instance";
            rs2 = stat2.executeQuery(sql2);


            while (rs2.next()) {
                DeviceInstanceEntity deviceInstanceEntity = new DeviceInstanceEntity();
                deviceInstanceEntity.setName(rs2.getString("name"));
                deviceInstanceEntity.setDescribe(rs2.getString("describe"));
                deviceInstanceEntity.setPhotoUrl(rs2.getString("photo_url"));
                ArrayList<DeviceInstanceEntity> deviceInstanceEntities = new ArrayList<>();
                deviceInstanceEntities.add(deviceInstanceEntity);
                examineTwoDataBaseEntity.setDeviceInstanceEntity(deviceInstanceEntities);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (com2 != null) {
                try {
                    com2.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stat2 != null) {
                try {
                    stat2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return getExamineUser().map(info -> {
            ArrayList<ExamineUserEntity> examineUserEntities = new ArrayList<>();
            examineUserEntities.add(info);
            examineTwoDataBaseEntity.setEntity(examineUserEntities);
            return examineTwoDataBaseEntity;
        });
    }


}
