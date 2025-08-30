package com.example.study.designpartten.strategy;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sc.nft.bo.PendingSettle.PendingSettleBO;
import com.sc.nft.config.yb.YbConstants;
import com.sc.nft.config.yb.bo.amount.AmountDivideBO;
import com.sc.nft.config.yb.response.amount.AmountDivideInfoResponse;
import com.sc.nft.config.yb.response.amount.AmountDivideResponse;
import com.sc.nft.entity.PendingSettle;
import com.sc.nft.entity.UserYbExt;
import com.sc.nft.enums.AccountStatusEnum;
import com.sc.nft.enums.PayChannelEnum;
import com.sc.nft.exception.GlobalRunTimeException;
import com.sc.nft.helper.YbPayHelper;
import com.sc.nft.mapper.UserYbExtMapper;
import com.sc.nft.strategy.PendingSettleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class PendingSettleYbStrategy extends PendingSettleStrategy {
    @Autowired
    UserYbExtMapper userYbExtMapper;
    @Autowired
    YbPayHelper ybPayHelper;

    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.YB_PAY;
    }

    @Override
    public Map<String, Object> getWalletInfo(Long userId) {
        UserYbExt userYbExt = userYbExtMapper.selectByUserId(userId);
        if (userYbExt == null || userYbExt.getStatus() != AccountStatusEnum.SUCCESS || StrUtil.isBlank(userYbExt.getWalletUserNo())) {
            log.error("【易宝支付】转到第三方钱包失败，易宝分账目标用户不存在 userId: {}", userId);
            throw new GlobalRunTimeException("易宝支付】转到第三方钱包失败，易宝分账目标用户不存在 userId: " + userId);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("walletUserNo", userYbExt.getWalletUserNo());
        return map;
    }

    @Override
    public PendingSettleBO settle(PendingSettle pendingSettle, Map<String, Object> walletInfo, String settleNo, BigDecimal amount) {
        PendingSettleBO pendingSettleBO = new PendingSettleBO();

        AmountDivideBO amountDivideBO = new AmountDivideBO();
        amountDivideBO.setOrderId(pendingSettle.getTradeNo());
        amountDivideBO.setDivideRequestId(settleNo);
        AmountDivideBO.DivideDetail divideDetail = amountDivideBO.new DivideDetail();
        divideDetail.setLedgerNo(String.valueOf(walletInfo.get("walletUserNo")));
        divideDetail.setAmount(amount.toPlainString());
        divideDetail.setLedgerType(YbConstants.LedgerType.MERCHANT2MEMBER);
        amountDivideBO.setDivideDetails(Lists.newArrayList(divideDetail));

        pendingSettleBO.setReqParams(JSONObject.toJSONString(amountDivideBO));
        try {
            AmountDivideResponse amountDivideResponse = ybPayHelper.divideAmount(amountDivideBO);

            List<AmountDivideResponse.DivideDetail> divideDetailList = amountDivideResponse.getDivideDetailList();
            if (divideDetailList != null && !divideDetailList.isEmpty() && divideDetailList.get(0) != null) {
                pendingSettleBO.setSettleDetailNo(divideDetailList.get(0).getDivideDetailNo());
            }
        } catch (Exception e) {
            pendingSettleBO.setFailDesc(e.getMessage());
        }
        return pendingSettleBO;
    }

    @Override
    public String getSettleDetailNo(String settleNo, String tradeNo) {
        AmountDivideInfoResponse amountDivideInfoResponse = ybPayHelper.getDivideInfo(settleNo, tradeNo);
        if (Objects.equals(amountDivideInfoResponse.getCode(), YbConstants.OPR_SUCCESS_CODE) &&
                amountDivideInfoResponse.getDivideDetailList() != null &&
                !amountDivideInfoResponse.getDivideDetailList().isEmpty()) {
            return amountDivideInfoResponse.getDivideDetailList().get(0).getDivideDetailNo();
        }
        return null;
    }
}
