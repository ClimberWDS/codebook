package com.wds.codebook.sharding.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * 精准分片算法：根据 create_time 分表，按月分表
 *
 * @author wds
 * @DateTime 2024/12/23 14:31
 */
@Component
public class TimeShardingTableAlgorithm implements PreciseShardingAlgorithm<String> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyyy_MM");
    private static DateTimeFormatter originFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(TimeShardingTableAlgorithm.class);

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        StringBuffer tableName = new StringBuffer();
        String value = shardingValue.getValue();  // 获取传入的时间字段（例如 create_time）

        LocalDateTime parse = LocalDateTime.parse(value, originFormat);  // 转换为 LocalDateTime 对象
        // 按照 _yyyy_MM 格式化时间
        String format = parse.format(formatter);
        tableName.append(shardingValue.getLogicTableName()).append(format);  // 拼接分表名

        logger.warn("doSharding->" + tableName);

        return tableName.toString();  // 返回最终的分表名
    }
}
