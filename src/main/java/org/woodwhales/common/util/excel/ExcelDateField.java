package org.woodwhales.common.util.excel;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author woodwhales on 2021-07-28 16:58
 * @description 日期属性excel注解
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface ExcelDateField {

    /**
     * excel列字段，默认为数据对象属性名
     * @return 列字段，默认为数据对象属性名
     */
    String value();

    /**
     * 当 type 为 Date.class 时，格式化
     * @return yyyy-MM-dd HH:mm:ss
     */
    String pattern() default "yyyy-MM-dd HH:mm:ss";

}
