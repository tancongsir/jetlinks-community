package org.jetlinks.community.work.manager.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hswebframework.ezorm.rdb.mapping.defaults.SaveResult;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportWorkOrderResult {

    private SaveResult result;

    private boolean success;

    private String message;

    public static ImportWorkOrderResult success(SaveResult result) {
        return new ImportWorkOrderResult(result, true, null);
    }

    public static ImportWorkOrderResult error(String message) {
        return new ImportWorkOrderResult(null, false, message);
    }

    public static ImportWorkOrderResult error(Throwable message) {
        return error(message.getMessage());
    }
}
