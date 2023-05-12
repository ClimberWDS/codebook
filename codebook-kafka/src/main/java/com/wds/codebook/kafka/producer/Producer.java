package com.wds.codebook.kafka.producer;

import com.alibaba.nacos.common.utils.JacksonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author wds
 * @date 2023-03-06 13:58
 **/
@Component
public class Producer {

    @Autowired
    private KafkaTemplate kafkaTemplate;


    //发送消息方法
    public void send() {
        Message message = new Message();
        message.setId("KFK_"+System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        kafkaTemplate.send("test", JacksonUtils.toJson(message));
    }


    public static void main(String[] args) {

    }

}
