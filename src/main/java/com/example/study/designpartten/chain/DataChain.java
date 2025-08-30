package com.example.study.designpartten.chain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataChain<T> {

    private final AbstractDataHandler<T> handler;


    public DataChain(List<AbstractDataHandler<T>> jobHandlers) {
        AbstractDataHandler.Builder<T> builder = new AbstractDataHandler.Builder<>();
        jobHandlers.forEach(
                builder::addHandler
        );
        handler = builder.build();
    }

    public void proceed(T object) {
        handler.doHandler(object);
    }
}
