package com.wds.codebook.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

public class RedisDemo {
    @Autowired
    StringRedisTemplate redisTemplate;

    String grayRedisKey1 = String.format(RedisKeys.OMS_APP_GRAY_VERSION,"8910","1");
    String grayRedisKey2 = String.format(RedisKeys.OMS_APP_GRAY_VERSION,"8910","2");
    String grayRedisKey3 = String.format(RedisKeys.OMS_APP_GRAY_VERSION,"8910","3");
    String grayRedisKey4 = String.format(RedisKeys.OMS_APP_GRAY_VERSION,"8910","4");
    String grayListRedisKey = String.format(RedisKeys.OMS_APP_GRAY_LIST,"8910");
    VersionRedisDto dto1 = new VersionRedisDto("",3L,10L,1,"123,231");
    VersionRedisDto dto2 = new VersionRedisDto("",4L,10L,1,"123,231");
    Map<String,String> map1 = new HashMap<>();
//        map1.put("versionCode","1");
//        map1.put("currReleaseNum","10");
//        map1.put("forceUpdate","1");
//    Map<String,String> map2 = new HashMap<>();
//        map2.put("versionCode","2");
//        map2.put("currReleaseNum","10");
//        map2.put("forceUpdate","1");
//
//        redisTemplate.opsForHash().putAll(grayRedisKey1,map1);
//        redisTemplate.opsForHash().putAll(grayRedisKey2,map2);
//        redisTemplate.opsForHash().putAll(grayRedisKey3, dto1.toMap());
//        redisTemplate.opsForHash().putAll(grayRedisKey4, dto2.toMap());
//    Map<Object, Object> entries = redisTemplate.opsForHash().entries(grayListRedisKey);
//        redisTemplate.opsForHash().increment(grayRedisKey1,"currReleaseNum",10);
//        redisTemplate.opsForHash().increment(grayRedisKey2,"currReleaseNum",-1);
//        System.out.println(entries.toString());
}
