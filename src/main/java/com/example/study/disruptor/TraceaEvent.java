package com.example.study.disruptor;

public class TraceaEvent {

    private String traceId;

    private String eventName;

    private String traceTime;

    public TraceaEvent(){
    }

    public TraceaEvent(String uuid, String eventName, String traceTime){
        this.traceId = uuid;
        this.eventName = eventName;
        this.traceTime = traceTime;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(String traceTime) {
        this.traceTime = traceTime;
    }
}

