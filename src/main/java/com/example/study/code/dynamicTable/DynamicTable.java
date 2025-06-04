package com.example.study.code.dynamicTable;

import java.lang.annotation.*;

/**
 * 分表注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DynamicTable {
    /**
     * 分表策略
     */
    Class<? extends TableShardingStrategy> strategy();

    /**
     * 分表参数，用于分表计算
     */
    String shardingParam() default "";
}