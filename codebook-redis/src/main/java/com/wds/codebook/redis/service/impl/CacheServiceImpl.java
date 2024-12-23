package com.wds.codebook.redis.service.impl;

import com.wds.codebook.redis.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;



/**
 * 缓存信息类 服务实现类
 *
 * @author wds
 * @since 2023-07-17
 */
@Slf4j
@Service
public class CacheServiceImpl implements CacheService {

    @Resource(name = "stringRedisTemplate")
    RedisTemplate<String, String> redisTemplate;

    @Resource
    private Redisson redisson;

    /**
     * 用户当日订单
     * 用户订单实时增加
     */
   public static final   String ORDER_DAY_USER = "order:day:%s:user:%s";
    /**
     * 用户订单记录 hashKey
     */
    public static final String ORDER_USER_HASH = "order:user:%s";
    /**
     * 更新用户订单记录锁
     */
    public static final String ORDER_USER_HASH_LOCK = "order:user:%s:lock";


    /**
     * 保存当日订单
     */
    @Override
    public void saveDayOrder(Integer uid, BigDecimal amount) {
        String dayCode = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        RLock lock = redisson.getLock(String.format(ORDER_USER_HASH_LOCK, dayCode, uid));
        try {
            lock.lock();
            String orderDayKey = String.format(ORDER_DAY_USER, dayCode, uid);
            if (Boolean.FALSE.equals(redisTemplate.hasKey(orderDayKey))) {
                redisTemplate.opsForValue().set(orderDayKey, amount.toString());
            }
            BigDecimal orderSum = new BigDecimal(redisTemplate.opsForValue().get(orderDayKey));
            redisTemplate.opsForValue().set(orderDayKey, orderSum.add(amount).toPlainString());
        }catch (Exception e) {
            log.error("保存当日订单异常", e);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void saveDayOrderLua(Integer uid, BigDecimal amount) {
        String dayCode = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderDayKey = String.format(ORDER_DAY_USER, dayCode, uid);

        // 使用 Lua 脚本操作保证原子性
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            byte[] keyBytes = orderDayKey.getBytes(StandardCharsets.UTF_8);
            byte[] currentValueBytes = connection.get(keyBytes);

            BigDecimal currentValue = (currentValueBytes == null)
                    ? BigDecimal.ZERO
                    : new BigDecimal(new String(currentValueBytes, StandardCharsets.UTF_8));

            BigDecimal newValue = currentValue.add(amount);
            connection.set(keyBytes, newValue.toPlainString().getBytes(StandardCharsets.UTF_8));
            return null;
        });
    }


    /**
     * 保存当日订单
     */
    @Override
    public void saveDayOrderIncr(Integer uid, BigDecimal amount) {
        String dayCode = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderDayKey = String.format(ORDER_DAY_USER, dayCode, uid);
        redisTemplate.opsForValue().setIfAbsent(orderDayKey, "0"); // 初始化为 0
        redisTemplate.opsForValue().increment(orderDayKey, amount.doubleValue()); // 原子性操作
    }

    /**
     *
     * @param uid
     * @param amount
     */
    @Override
    public void saveDayOrder(Integer uid, BigDecimal amount,int showPrecision) {
        String dayCode = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderDayKey = String.format(ORDER_DAY_USER, dayCode, uid);
        ;
        // 将金额转化为 "分" 存储，保证使用整数操作
        long amountInCents = amount.multiply(BigDecimal.TEN.pow(showPrecision)).longValue();
        redisTemplate.opsForValue().increment(orderDayKey, amountInCents);
    }

    public BigDecimal getDayOrder(Integer uid,String dayCode) {
        String orderDayKey = String.format(ORDER_DAY_USER, dayCode, uid);

        String value = redisTemplate.opsForValue().get(orderDayKey);
        if (value == null) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(value).divide(BigDecimal.valueOf(100)); // 转回元单位
    }



    /**
     * 保存用户订单Hash
     * 1. 获取前一天的订单数据
     * 2. 获取最早一天的订单数据
     * 3. 更新用户前29天汇总数据 = sumAmount +前一天的订单数据 - 最早一天的订单数据
     * 4. 保存前一天的订单数据
     * 5. 删除最早一天的订单数据
     */
    @Override
    public void updateUserOrder(Integer uid) {
        RLock lock = redisson.getLock(String.format(ORDER_USER_HASH_LOCK,  uid));
        try {
            lock.lock();
            String preDayCode = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 前一天日期
            BigDecimal preDayorder = getDayOrder(uid, preDayCode);

            String pre30DayCode = LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 最早一天日期
            BigDecimal oldestDayorder = getDayOrder(uid, pre30DayCode);

            String orderKey = String.format(ORDER_USER_HASH, uid); // 用户的 Hash key
            BigDecimal sumAmount = getHashValueAsBigDecimal(orderKey);
            sumAmount = sumAmount.add(preDayorder).subtract(oldestDayorder);

            // 更新 Redis 数据
            redisTemplate.opsForHash().put(orderKey, "sumAmount", sumAmount.toPlainString()); // 更新汇总值
            if (preDayorder.compareTo(BigDecimal.ZERO) > 0) {
                redisTemplate.opsForHash().put(orderKey, preDayCode, preDayorder.toPlainString()); // 添加前一天数据
            }
            redisTemplate.opsForHash().delete(orderKey, pre30DayCode);
        }catch (Exception e){
            log.error("updateUserorder error", e);
        }finally {
            lock.unlock();
        }
    }
    private BigDecimal getHashValueAsBigDecimal(String hashKey) {
        Object value = redisTemplate.opsForHash().get(hashKey, "sumAmount");
        return value == null ? BigDecimal.ZERO : new BigDecimal(value.toString());
    }



    public void updateUserorderLua(Integer uid) {
        String luaScript =
                "local orderKey = KEYS[1]; " +
                        "local preDayCode = ARGV[1]; " +
                        "local pre30DayCode = ARGV[2]; " +
                        "local preDayorder = tonumber(ARGV[3]); " +
                        "local sumAmount = tonumber(redis.call('hget', orderKey, 'sumAmount') or '0'); " +
                        "local oldestDayorder = tonumber(redis.call('hget', orderKey, pre30DayCode) or '0'); " +
                        "sumAmount = sumAmount + preDayorder - oldestDayorder; " +
                        "redis.call('hset', orderKey, 'sumAmount', tostring(sumAmount)); " +
                        "if preDayorder > 0 then " +
                        "    redis.call('hset', orderKey, preDayCode, tostring(preDayorder)); " +
                        "end; " +
                        "redis.call('hdel', orderKey, pre30DayCode); " +
                        "return sumAmount; ";

        String orderKey = String.format(ORDER_USER_HASH, uid);
        String preDayCode = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String pre30DayCode = LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 获取前一天的订单金额（以分为单位存储）
        BigDecimal preDayorder = getDayOrder(uid, preDayCode).multiply(BigDecimal.valueOf(100));

        // 执行 Lua 脚本
        RedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);
        Long result = redisTemplate.execute(
                script,
                Collections.singletonList(orderKey),
                preDayCode, pre30DayCode, preDayorder.toPlainString()
        );

        log.info("Updated user order for UID: {}. New sumAmount: {}", uid, result);
    }


    public void addNewOrderUser(Integer uid) {
        // 获取当前时间的分钟数，用于生成 Redis key
        String minuteKey = String.valueOf(System.currentTimeMillis() / (5 * 60 * 1000)); // 当前 5 分钟区间的标识
        String redisKey = String.format("new_order_users:%s", minuteKey);

        // 将用户 uid 添加到 Redis 的 Set 中
        redisTemplate.opsForSet().add(redisKey, uid.toString());

        // 设置过期时间，防止历史数据积累，设置为6分钟
        redisTemplate.expire(redisKey, Duration.ofMinutes(6));
    }


    public Set<Integer> getNewOrderUser(Integer uid) {
        String currentMinuteKey = String.valueOf(System.currentTimeMillis() / (5 * 60 * 1000));
        String redisKey = String.format("new_order_users:%s", currentMinuteKey);
        // 获取所有在 5 分钟内新增订单的用户 UID
        Set<String> userIds = redisTemplate.opsForSet().members(redisKey);
        return userIds.stream().map(Integer::valueOf).collect(Collectors.toSet());

    }


    public void addNewOrderUser1(Integer uid) {
        String redisKey = "new_order_users";

        // 当前时间戳作为分数
        double timestamp = System.currentTimeMillis();

        // 将用户 uid 添加到 Redis ZSet
        redisTemplate.opsForZSet().add(redisKey, uid.toString(), timestamp);

        // 设置 ZSet 的过期时间（假设保留 10 分钟的数据）
        redisTemplate.expire(redisKey, Duration.ofMinutes(10));
    }

    public Set<Integer> getNewOrderUser1(Integer uid) {
        String redisKey = "new_order_users";

        // 获取当前时间戳
        double now = System.currentTimeMillis();
        double fiveMinutesAgo = now - 5 * 60 * 1000; // 5 分钟前的时间戳

        // 获取过去 5 分钟内的用户 UID
        Set<String> userIds = redisTemplate.opsForZSet().rangeByScore(redisKey, fiveMinutesAgo, now);
        if (userIds == null || userIds.isEmpty()) {
            log.info("No new users to process in the last 5 minutes.");
            return Collections.emptySet();
        }
        return userIds.stream().map(Integer::valueOf).collect(Collectors.toSet());
    }



    /**
     * 获取价格
     *
     * @param coin       币种
     * @param secondTime 时间（精确到秒）
     * @return 币种价格
     */
    @Override
    public BigDecimal getCoinPrice(String coin, Long secondTime) {
        return getCoinPrice(coin, secondTime, secondTime - 60 );
    }

    /**
     * 获取价格
     *
     * @param coin       币种
     * @param secondTime 时间（精确到秒）
     * @param openTime   开仓时间/起始时间
     * @return 币种价格
     */
    @Override
    public BigDecimal getCoinPrice(String coin, Long secondTime, Long openTime) {

        String redisKey = String.format("COIN_SECOND_PRICE:%s", coin.toUpperCase());
        Object currPrice = redisTemplate.opsForHash().get(redisKey, secondTime.toString());
        if (Objects.nonNull(currPrice)) {
            log.info("secondTime：{},价格：{}", secondTime, currPrice);
            return new BigDecimal(String.valueOf(currPrice));
        }

        List<String> timeKeys = LongStream.rangeClosed(openTime, secondTime)
                .boxed()
                .sorted(Comparator.reverseOrder()) // 按时间从大到小排序
                .map(String::valueOf)
                .collect(Collectors.toList());

        // 一次性从 Redis 获取多个时间戳的价格
        List<Object> prices = redisTemplate.opsForHash().multiGet(redisKey, new ArrayList<>(timeKeys));
        // 查找第一个非空的价格
        for (Object price : prices) {
            if (Objects.nonNull(price)) {
                log.info("价格：{}", price);
                return new BigDecimal(String.valueOf(price));
            }
        }
        return null;
    }

}
