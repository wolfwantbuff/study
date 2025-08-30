package com.example.study.disruptor;

import com.lmax.disruptor.EventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.slf4j.MDC;

public class TraceEventHandler implements EventHandler {
    private final static Marker MARKER = MarkerManager.getMarker("Const.MARKER");
    private final static Logger logger = LogManager.getLogger("Const.MARKER");

    @Override
    public void onEvent(Object o, long l, boolean b) throws Exception {
        TraceaEvent traceaEvent = (TraceaEvent) o;
        MDC.put("hostIp", "HOST_IP");
        MDC.put("traceId", traceaEvent.getTraceId());
        MDC.put("eventName", traceaEvent.getEventName());
        MDC.put("eventTime", traceaEvent.getTraceTime());
        String moduleName = System.getProperty("moduleName");
        if (moduleName == null || "".equals(moduleName)) {
            String[] split = System.getProperties().getProperty("sun.java.command").split("\\.");
            moduleName = split[split.length - 1];
            System.setProperty("moduleName",moduleName);
        }
        MDC.put("moduleName", moduleName);
        logger.info(MARKER, "Const.MARKER");
        MDC.clear();
    }
}
