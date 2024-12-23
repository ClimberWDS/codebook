package com.wds.codebook.redis.service;

import java.math.BigDecimal;

/**
 * 滑动时间窗口
 * @author: wds
 * @DateTime: 2024/11/25 11:04
 */
public interface CacheService {
    void saveDayOrderLua(Integer uid, BigDecimal amount);

    void saveDayOrder(Integer uid, BigDecimal amount);

    void saveDayOrderIncr(Integer uid, BigDecimal amount);

    void saveDayOrder(Integer uid, BigDecimal amount, int showPrecision);

    void updateUserOrder(Integer uid);
    BigDecimal getCoinPrice(String coin, Long secondTime);

    BigDecimal getCoinPrice(String coin, Long secondTime, Long openTime);
}
