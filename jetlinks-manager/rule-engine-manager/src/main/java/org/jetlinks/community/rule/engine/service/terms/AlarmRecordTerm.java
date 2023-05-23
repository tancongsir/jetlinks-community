package org.jetlinks.community.rule.engine.service.terms;

import org.hswebframework.ezorm.core.param.Term;
import org.hswebframework.ezorm.rdb.metadata.RDBColumnMetadata;
import org.hswebframework.ezorm.rdb.metadata.RDBTableMetadata;
import org.hswebframework.ezorm.rdb.metadata.TableOrViewMetadata;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.*;
import org.hswebframework.ezorm.rdb.operator.builder.fragments.term.AbstractTermFragmentBuilder;
import org.jetlinks.community.utils.ConverterUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 告警查询规则.
 * <p>
 * 例如：查询设备名为test1的告警记录
 * <pre>
 * {
 * "terms": [
 *      {
 *          "column": "target_id$alarm-record-term",
 *          "value": [
 *              {
 *              "column": "alarmName",
 *              "termType": "eq",
 *              "value": "test1"
 *              }
 *          ]
 *       }
 *    ]
 * }
 * </pre>
 *
 * @author TanCong 2023/5/11
 */
@Component
public class AlarmRecordTerm extends AbstractTermFragmentBuilder {


    public AlarmRecordTerm() {
        super("alarm-record-term", "告警记录查询规则");
    }

    @Override
    public SqlFragments createFragments(String columnFullName,
                                        RDBColumnMetadata column,
                                        Term term) {
        List<Term> terms = ConverterUtils.convertTerms(term.getValue());
        PrepareSqlFragments sqlFragments = PrepareSqlFragments.of();
        if (term.getOptions().contains("not")) {
            sqlFragments.addSql("not");
        }
        sqlFragments
            .addSql("exists(select 1 from ", getTableName("dev_device_instance", column), " _dev where _dev.product_id =", columnFullName);


        RDBTableMetadata metadata = column
            .getOwner()
            .getSchema()
            .getTable("dev_device_instance")
            .orElseThrow(() -> new UnsupportedOperationException("unsupported dev_device_instance"));

        SqlFragments where = builder.createTermFragments(metadata, terms);
        if (!where.isEmpty()) {
            sqlFragments.addSql("and")
                .addFragments(where);
        }

        sqlFragments.addSql(")");

        return sqlFragments;
    }

    static DeviceTermsBuilder builder = new DeviceTermsBuilder();

    static class DeviceTermsBuilder extends AbstractTermsFragmentBuilder<TableOrViewMetadata> {

        @Override
        protected SqlFragments createTermFragments(TableOrViewMetadata parameter,
                                                   List<Term> terms) {
            return super.createTermFragments(parameter, terms);
        }

        @Override
        protected SqlFragments createTermFragments(TableOrViewMetadata table,
                                                   Term term) {
            if (term.getValue() instanceof NativeSql) {
                NativeSql sql = ((NativeSql) term.getValue());
                return PrepareSqlFragments.of(sql.getSql(), sql.getParameters());
            }
            return table
                .getColumn(term.getColumn())
                .flatMap(column -> table
                    .findFeature(TermFragmentBuilder.createFeatureId(term.getTermType()))
                    .map(termFragment -> termFragment.createFragments(column.getFullName("_dev"), column, term)))
                .orElse(EmptySqlFragments.INSTANCE);
        }
    }
}
