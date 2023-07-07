package org.jetlinks.community.es.config;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetlinks.community.elastic.search.index.DefaultElasticSearchIndexMetadata;
import org.jetlinks.community.elastic.search.index.ElasticSearchIndexManager;
import org.jetlinks.community.es.ExamineESEnum.ExamineIndexEnum;
import org.jetlinks.core.metadata.types.DateTimeType;
import org.jetlinks.core.metadata.types.StringType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Setter
@Component
@AllArgsConstructor
public class Configurations implements CommandLineRunner {
    //引入indexManager
    private final ElasticSearchIndexManager indexManager;

    @SneakyThrows
    @Override
    public void run(String... args) {
        indexManager.putIndex(
                //设置es自定义模板名称
                new DefaultElasticSearchIndexMetadata(ExamineIndexEnum.custom.getIndex())
                        //添加自定义模板属性
                        .addProperty("name", new StringType())
                        .addProperty("value", new StringType())
                        .addProperty("timestamp", new DateTimeType())
        ).subscribe();
    }

}