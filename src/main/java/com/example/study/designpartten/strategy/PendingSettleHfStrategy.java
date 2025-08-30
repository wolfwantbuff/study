package com.example.study.designpartten.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.sc.nft.bo.PendingSettle.PendingSettleBO;
import com.sc.nft.dao.DaoCustomerOpenAccountDao;
import com.sc.nft.dao.PayOrderDao;
import com.sc.nft.entity.DaoCustomerOpenAccount;
import com.sc.nft.entity.PayOrder;
import com.sc.nft.entity.PendingSettle;
import com.sc.nft.enums.*;
import com.sc.nft.exception.GlobalRunTimeException;
import com.sc.nft.helper.HfPayHelper;
import com.sc.nft.strategy.PendingSettleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PendingSettleHfStrategy extends PendingSettleStrategy {

    @Autowired
    DaoCustomerOpenAccountDao customerOpenAccountDao;
    @Autowired
    HfPayHelper hfPayHelper;
    @Autowired
    PayOrderDao payOrderDao;

    @Override
    public PayChannelEnum getChannel() {
        // 汇付支付
        return PayChannelEnum.HF_PAY;
    }

    @Override
    public Map<String, Object> getWalletInfo(Long daoId) {
        DaoCustomerOpenAccount openAccount = customerOpenAccountDao.getByChannel(daoId, WalletChannelEnum.HF, AccountStatusEnum.SUCCESS);
        if (ObjectUtil.isNull(openAccount)) {
            log.error("【汇付支付】转到第三方钱包失败，此商户未开户 daoId: {}", "");
            throw new GlobalRunTimeException("【汇付支付】转到第三方钱包失败，此商户未开户 daoId: " + daoId);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("openAccount", openAccount);
        return result;
    }

    @Override
    public PendingSettleBO settle(PendingSettle pendingSettle, Map<String, Object> walletInfo, String settleNo, BigDecimal amount) {
        PayOrder payOrder = payOrderDao.getById(pendingSettle.getPayOrderId());

        PendingSettleBO pendingSettleBO = new PendingSettleBO();
        try {
            DaoCustomerOpenAccount openAccount = (DaoCustomerOpenAccount) walletInfo.get("openAccount");
            HfPayHelper.DaoHfAccount account = HfPayHelper.of(openAccount);
            HfPayHelper.Context ctx = new HfPayHelper.Context();
            JSONObject result = hfPayHelper.splitMoney(ctx, account, pendingSettle.getTradeNo(), payOrder.getPayTime(),
                    HfPayHelper.getHfTransType(payOrder.getPayType()), amount);
            pendingSettleBO.setReqParams(JSONObject.toJSONString(ctx.getParams()));
            if (!HfPayHelper.isSuccess(result)) {
                throw new GlobalRunTimeException("汇付分账失败：" + result.getString("resp_desc"));
            }
            pendingSettleBO.setSettleDetailNo(result.getString("platform_seq_id"));
        } catch (Exception e) {
            pendingSettleBO.setFailDesc(e.getMessage());
        }
        return pendingSettleBO;
    }


    @Override
    public String getSettleDetailNo(String settleNo, String tradeNo) {
        // 汇付默认都成功
        return "0000";
    }
}
