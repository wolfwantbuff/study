package com.example.study.designpartten.strategy;

import cn.hutool.core.util.IdUtil;
import com.sc.nft.bo.PendingSettle.PendingSettleBO;
import com.sc.nft.bo.PendingSettle.PendingSettleChainBO;
import com.sc.nft.chain.pendingSettle.PendingSettleChain;
import com.sc.nft.entity.PendingSettle;
import com.sc.nft.enums.PayChannelEnum;
import com.sc.nft.exception.GlobalRunTimeException;
import com.sc.nft.mapper.PendingSettleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public abstract class PendingSettleStrategy {
    @Autowired
    PendingSettleChain pendingSettleChain;
    @Autowired
    PendingSettleMapper pendingSettleMapper;

    public abstract PayChannelEnum getChannel();

    public abstract Map<String, Object> getWalletInfo(Long userId);

    /**
     * 分账
     *
     * @param pendingSettle
     * @param walletInfo
     * @param settleNo
     * @param amount
     * @return
     */
    public abstract PendingSettleBO settle(PendingSettle pendingSettle, Map<String, Object> walletInfo, String settleNo, BigDecimal amount);

    /**
     * 获取第三方分账流水号
     *
     * @param settleNo
     * @param tradeNo
     * @return
     */
    public abstract String getSettleDetailNo(String settleNo, String tradeNo);

    public BigDecimal maxSettleAmount(BigDecimal settlAmountTotal, List<PendingSettle> settleData, Integer page) {
        List<PendingSettle> pendingSettles = pendingSettleMapper.selectListAvailableSettleRecordByChannel(getChannel(), page, 1000);
        if (pendingSettles.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal PlusResultOfsettlAmount = BigDecimal.ZERO;
        for (PendingSettle pendingSettle : pendingSettles) {
            if (PlusResultOfsettlAmount.compareTo(settlAmountTotal) >= 0) {
                break;
            }
            PlusResultOfsettlAmount = PlusResultOfsettlAmount.add(pendingSettle.getLeftAmount());
            settleData.add(pendingSettle);
        }
        if (PlusResultOfsettlAmount.compareTo(settlAmountTotal) >= 0) {
            return settlAmountTotal;
        } else {
            return PlusResultOfsettlAmount.add(maxSettleAmount(settlAmountTotal.subtract(PlusResultOfsettlAmount), settleData, page + 1));
        }
    }

    /**
     * 通过分账实现平台钱包(dao_wallet)转入第三方钱包
     *
     * @param settlAmountTotal
     * @param userId
     * @param daoTransferId
     */

    public void transferBySettle(BigDecimal settlAmountTotal, Long userId, Long daoTransferId) {
        List<PendingSettle> pendingSettles = new ArrayList<>();
        BigDecimal maxSettleAmount = maxSettleAmount(settlAmountTotal, pendingSettles, 0);
        if (maxSettleAmount.compareTo(settlAmountTotal) < 0) {
            throw new GlobalRunTimeException("最大转账金额：" + maxSettleAmount.toPlainString());
        }

        try {
            doTransferBySettle(settlAmountTotal, userId, daoTransferId, pendingSettles);
        } catch (Exception e) {
            log.error("平台钱包转入第三方账户异常，定时任务补偿处理", e);
        }
    }

    public void doTransferBySettle(BigDecimal settlAmountTotal, Long userId, Long daoTransferId, List<PendingSettle> pendingSettles) {
        for (PendingSettle pendingSettle : pendingSettles) {
            if (settlAmountTotal.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal settleAmount;
            BigDecimal leftAmountAfterSubstract;
            if (pendingSettle.getLeftAmount().compareTo(settlAmountTotal) >= 0) {
                settleAmount = settlAmountTotal;
                leftAmountAfterSubstract = pendingSettle.getLeftAmount().subtract(settlAmountTotal);
            } else {
                settleAmount = pendingSettle.getLeftAmount();
                leftAmountAfterSubstract = BigDecimal.ZERO;
            }

            settlAmountTotal = settlAmountTotal.subtract(settleAmount);

            String settleNo = IdUtil.getSnowflakeNextIdStr();
            PendingSettleChainBO pendingSettleChainBO = new PendingSettleChainBO();
            pendingSettleChainBO.setUserId(userId);
            pendingSettleChainBO.setWalletInfo(getWalletInfo(userId));
            pendingSettleChainBO.setSettleNo(settleNo);
            pendingSettleChainBO.setSettleAmount(settleAmount);
            pendingSettleChainBO.setLeftAmountAfterSubstract(leftAmountAfterSubstract);
            pendingSettleChainBO.setOver(BigDecimal.ZERO.compareTo(leftAmountAfterSubstract) >= 0);
            pendingSettleChainBO.setPendingSettle(pendingSettle);
            pendingSettleChainBO.setPendingSettleStrategy(this);
            pendingSettleChainBO.setDaoTransferId(daoTransferId);
            pendingSettleChain.proceed(pendingSettleChainBO);
        }
    }

}
