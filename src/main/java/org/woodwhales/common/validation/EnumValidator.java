package org.woodwhales.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验枚举值的校验注解
 * @author woodwhales
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = EnumValidatorValidator.class)
public @interface EnumValidator {

    /**
     * 枚举Class对象
     * @return
     */
	Class<? extends Enum>[] target() default {};

    /**
     * 枚举中要校验的数值的对应方法
     * 默认值为：Enum 类 的 name() 方法
     */
    String methodName() default "name";

    /**
     * 校验失败默认异常信息
     * @return
     */
    String message() default "入参值不在正确枚举中";
    
    // 以下两行为固定模板
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}