package com.example.study.designpartten.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PendingSettleStrategyFactory {
    public static Map<PayChannelEnum, PendingSettleStrategy> map= new HashMap<>();
    @Autowired
    public void init(List<PendingSettleStrategy> pendingSettleStrategies) {
        for (PendingSettleStrategy pendingSettleStrategy : pendingSettleStrategies) {
            map.put(pendingSettleStrategy.getChannel(), pendingSettleStrategy);
        }
    }

    public static PendingSettleStrategy getStrategy(PayChannelEnum payChannel) {
        PendingSettleStrategy strategy = map.get(payChannel);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的支付渠道: " + payChannel);
        }
        return strategy;
    }
}
