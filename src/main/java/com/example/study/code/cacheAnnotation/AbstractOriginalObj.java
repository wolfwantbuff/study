package com.example.study.code.cacheAnnotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;

/**
 * @author xiaodong
 * @date 2022/8/12 10:58
 * @wiki
 */

@Configuration
public abstract class AbstractOriginalObj<T> {
    private T singleton;
    @Bean
    public T t(){
        try {
            singleton = getRClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return singleton;
    }

    public T getOriginalObj(){
        return singleton;
    }

    private Class<T> getRClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
