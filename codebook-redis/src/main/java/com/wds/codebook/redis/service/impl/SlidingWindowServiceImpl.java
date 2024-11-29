package com.wds.codebook.redis.service.impl;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import com.wds.codebook.redis.service.SlidingWindowService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 滑动时间窗口实现
 * @author wds
 * @DateTime: 2024/11/25 11:03
 */
public class SlidingWindowServiceImpl implements SlidingWindowService {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final String STREAM_KEY = "orders_stream";

    // 添加订单到 Stream
    public void addOrder(String orderId, double amount) {
        Map<String, Object> message = new HashMap<>();
        message.put("orderId", orderId);
        message.put("amount", amount);
        message.put("timestamp", System.currentTimeMillis());

        redisTemplate.opsForStream().add(
                StreamRecords.newRecord()
                        .in(STREAM_KEY)
                        .ofMap(message)
        );
    }

    // 查询过去 30 分钟内的订单
    public List<Map<Object, Object>> getRecentOrders() {
        long currentTime = System.currentTimeMillis();
        long fromTime = currentTime - 30 * 60 * 1000; // 30 分钟前

        // 获取 Stream 中指定范围的记录
        List<MapRecord<String, Object, Object>> records = redisTemplate.opsForStream()
                .range(STREAM_KEY, Range.closed(fromTime + "-0", currentTime + "-0"));

        // 转换记录的值为 List<Map<Object, Object>>
        if (records != null) {
            return records.stream()
                    .map(MapRecord::getValue) // 提取记录的值部分
                    .collect(Collectors.toList());
        }
        return Collections.emptyList(); // 如果没有数据，返回空列表
    }

    // 清理超过 30 分钟的数据
    public void cleanUpOldOrders() {
        long currentTime = System.currentTimeMillis();
        long fromTime = currentTime - 30 * 60 * 1000; // 30 分钟前

        // 使用 Range.before() 指定清理范围
        Range<String> range = Range.leftOpen("0", fromTime + "-0");

        // 删除超时的订单
        redisTemplate.opsForStream().delete(STREAM_KEY, String.valueOf(range));
    }


    public void cleanUpOldOrders1() {
        long currentTime = System.currentTimeMillis();
        long fromTime = currentTime - 30 * 60 * 1000; // 30 分钟前

        // 获取范围外的记录
        List<MapRecord<String, Object, Object>> records = redisTemplate.opsForStream()
                .range(STREAM_KEY, Range.leftOpen("0", fromTime + "-0"));

        if (records != null) {
            // 删除这些记录
            records.forEach(record -> redisTemplate.opsForStream().delete(STREAM_KEY, record.getId()));
        }
    }

}