package org.jetlinks.community.web.excel;

import lombok.Getter;
import lombok.Setter;
import org.hswebframework.reactor.excel.CellDataType;
import org.hswebframework.reactor.excel.ExcelHeader;
import org.hswebframework.web.bean.FastBeanCopier;
import org.jetlinks.community.entity.ExamineUserEntity;

import java.util.*;

@Getter
@Setter
public class ExamineUserExcelInfo {

    private String id;
    private String password;
    private String type;
    private String name;
    private String status;
    private String username;

    private List<ExamineUserExcelInfo> tags = new ArrayList<>();

    private long rowNumber;

    public Map<String,Object> toMap(){
        return FastBeanCopier.copy(this,new HashMap<>());
    }


    public void with(String key, Object value) {
        FastBeanCopier.copy(Collections.singletonMap(key, value), this);
    }

    public static List<ExcelHeader> getExportHeaderMapping(List<ExamineUserEntity> tags) {
        long time = new Date().getTime();
        return new ArrayList<>(Arrays.asList(
            new ExcelHeader("id", "ID"+time, CellDataType.STRING),
            new ExcelHeader("password", "密码", CellDataType.STRING),
            new ExcelHeader("type", "类型", CellDataType.STRING),
            new ExcelHeader("name", "姓名", CellDataType.STRING),
            new ExcelHeader("status", "状态", CellDataType.STRING),
            new ExcelHeader("username", "用户名", CellDataType.STRING)
        ));
    }

    public static Map<String, String> getImportHeaderMapping() {
        Map<String, String> mapping = new HashMap<>();

        mapping.put("ID", "id");
        mapping.put("密码", "password");
        mapping.put("类型", "type");
        mapping.put("姓名", "name");
        mapping.put("状态", "status");
        mapping.put("用户名", "username");

        return mapping;
    }
}
