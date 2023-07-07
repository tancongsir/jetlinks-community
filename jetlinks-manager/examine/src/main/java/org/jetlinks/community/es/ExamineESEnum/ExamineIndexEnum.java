package org.jetlinks.community.es.ExamineESEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamineIndexEnum {

    custom("custom_index");

    private String index;
}