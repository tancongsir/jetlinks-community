package org.jetlinks.community.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.reactor.excel.ReactorExcel;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.Permission;
import org.hswebframework.web.authorization.annotation.DeleteAction;
import org.hswebframework.web.authorization.annotation.QueryAction;
import org.hswebframework.web.bean.FastBeanCopier;
import org.hswebframework.web.crud.service.ReactiveCrudService;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.entity.ExamineTwoDataBaseEntity;
import org.jetlinks.community.entity.ExamineUserEntity;
import org.jetlinks.community.servive.ExamineUserService;
import org.jetlinks.community.web.excel.ExamineUserExcelInfo;
import org.jetlinks.core.event.EventBus;
import org.springframework.boot.CommandLineRunner;
import org.hswebframework.web.authorization.exception.UnAuthorizedException;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@RequestMapping("/examine")
@RestController
//@Authorize
//@Resource(id = "user-role", name = "用户角色管理")
@Tag(name = "examine接口")
@AllArgsConstructor
public class ExamineController implements ReactiveServiceCrudController<ExamineUserEntity, String>, CommandLineRunner {

    private final ExamineUserService service;

    private final EventBus eventBus;

    private final DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

    @Override
    public ReactiveCrudService<ExamineUserEntity, String> getService() {
        return service;
    }

    //examine-todo 第1题
    @QueryAction
    @Operation(summary = "用户查询")
    @GetMapping("/user/_query")
    public Mono<PagerResult<ExamineUserEntity>> getWordOrder(QueryParamEntity query) {
        return service.joinQueryExamineUserEntity(query);
    }


    //examine-todo 第11题
    @QueryAction
    @Operation(summary = "导出")
    @GetMapping("/excel.{format}")
    public Mono<Void> wordOrderExcel(ServerHttpResponse response,
                                     @Parameter(hidden = true) QueryParamEntity parameter,
                                     @PathVariable @Parameter(description = "文件格式,支持csv,xlsx") String format) throws IOException {
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=".concat(URLEncoder.encode("用户管理数据" + "-" + new Date().getTime() + "." + format, StandardCharsets.UTF_8
                .displayName())));
        return ReactorExcel.<ExamineUserExcelInfo>writer(format)
            .headers(ExamineUserExcelInfo.getExportHeaderMapping(Collections.emptyList()))
            .converter(ExamineUserExcelInfo::toMap)
            .writeBuffer(service
                .query(parameter)
                .map(entity -> FastBeanCopier.copy(entity, new ExamineUserExcelInfo(), "state")), 512 * 1024)
            .doOnError(err -> log.error(err.getMessage(), err))
            .map(bufferFactory::wrap)
            .as(response::writeWith);
    }

    //examine-todo 第7题
    @QueryAction
    @Operation(summary = "查询两个数据源的数据")
    @GetMapping("/two/data")
    public Flux<ExamineTwoDataBaseEntity> findTwoDataBase() {
        return service.findAll();
    }

    //examine-todo 第8题
    @GetMapping("/query/data")
    public Flux<ExamineUserEntity> findAll(QueryParamEntity param) {
        return Authentication
            .currentReactive()
            .switchIfEmpty(Mono.error(UnAuthorizedException::new))
            .map(auth -> {
                boolean a = false;
                boolean b = false;
                List<Permission> permissions = auth.getPermissions();
                for (Permission permission : permissions) {
                    if (permission.getId().equals("role")){
                        for (String action : permission.getActions()) {
                            if (action.equals("delete")) {
                                a = true;
                                break;
                            }
                        }
                    }
                    if (permission.getId().equals("user")){
                        for (String action : permission.getActions()) {
                            if (action.equals("query")){
                                b = true;
                                break;
                            }
                        }
                    }
                }
                return a && b;
            })
            .flatMapMany(bl -> {
                if (bl.equals(false)){
                    return Flux.error(() -> new Exception("权限配置错误"));
                }
                return service.query(param);
            });
    }

    //examine-todo 第9题
    @Override
    public void run(String... args) throws Exception {
//        Flux.interval(Duration.ofMillis(3000)).flatMap(i -> {
//                ExamineUserEntity examineUserEntity = new ExamineUserEntity();
//                examineUserEntity.setUsername("test");
//                examineUserEntity.setType("test");
//                examineUserEntity.setName("test");
//                examineUserEntity.setPassword("test");
//                examineUserEntity.setStatus(1);
//                examineUserEntity.setSalt("test");
//            return add(Mono.just(examineUserEntity))
//                .then(eventBus.publish("/examineuser/add",examineUserEntity));
//        })
//            .onErrorResume(err -> Mono.empty())
//            .subscribe();
    }
}
