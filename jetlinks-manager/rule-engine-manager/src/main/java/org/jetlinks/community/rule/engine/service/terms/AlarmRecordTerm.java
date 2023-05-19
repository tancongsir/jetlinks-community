package org.jetlinks.community.rule.engine.service.terms;

import org.hswebframework.ezorm.core.param.Term;
import org.hswebframework.ezorm.rdb.metadata.RDBColumnMetadata;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.term.AbstractTermFragmentBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
/**
 * 告警查询规则.
 *
 * 例如：查询设备名为test1的告警记录
 * <pre>
 *     {
 *  "terms":[
 *      {
 *          "column":"target_name",
 *          "termType":"alarm-record-term",
 *          "value":"test1"
 *      }
 *  ]
 * }
 * </pre>
 *
 * @author TanCong 2023/5/11
 */
@Component
public class AlarmRecordTerm extends AbstractTermFragmentBuilder {


    public AlarmRecordTerm() {
        super("alarm-record-term","告警记录查询规则");
    }

    @Override
    public SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term) {
        PrepareSqlFragments sqlFragments = PrepareSqlFragments.of();
        if (term.getOptions().contains("not")) {
            sqlFragments.addSql("not");
        }
        sqlFragments
            .addSql("exists(select 1 from ", getTableName("dev_product", column), " _bind where _bind.id =", columnFullName);

        List<Object> ruleId = convertList(column, term);
        sqlFragments
            .addSql(
                "and _bind.name in (",
                ruleId.stream().map(r -> "?").collect(Collectors.joining(",")),
                ")")
            .addParameter(ruleId);

        sqlFragments.addSql(")");

        return sqlFragments;
    }
}
