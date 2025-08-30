package com.example.study.disruptor;

import com.lmax.disruptor.EventFactory;

public class TraceEventFactory implements EventFactory {
    public TraceaEvent newInstance()
    {
        return new TraceaEvent();
    }
}