package com.example.study.code.cacheAnnotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CachePutCustom {
    String prefix() default "TABLE";
    String tableName() default "";
    String methodName() default "";
    String key() default "";
}
