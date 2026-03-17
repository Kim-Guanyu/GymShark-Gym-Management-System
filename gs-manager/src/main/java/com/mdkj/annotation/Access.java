package com.mdkj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Access {
    // 原有的角色配置
    String[] value() default {};
    
    // 新增：允许访问的用户ID列表（默认空数组）
    long[] ids() default {};
}