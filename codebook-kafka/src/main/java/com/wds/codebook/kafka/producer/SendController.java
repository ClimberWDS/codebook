package com.wds.codebook.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 
 * @author wds
 * @date 2022-07-13 20:44
 **/
@RestController
@RequestMapping("/mess")
@Slf4j
public class SendController {



    @Autowired
    private  KafkaTemplate kafkaTemplate;

    @Autowired
    private Producer producer;

    @GetMapping( "/send")
    public String send() {
        log.info("请求发送消息！");
        producer.send();
        return "{\"code\":0}";
    }


    @GetMapping( "/sendMsgToDoris")
    public String sendMsgToDoris(Integer num) {
        log.info("请求发送消息！");
        num= num==null||num<0?100:num;
        for(int i=0;i<num;i++){
            kafkaTemplate.send("test-doris-topic", JacksonUtils.toJson(ExampleDto.genExampleDto()));
        }
        return "{\"code\":0}";
    }


//    @GetMapping("/kafka/callbackOne/{message}")
//    public void sendMessage2(@PathVariable("message") String callbackMessage) {
//        kafkaTemplate.send("topic1", callbackMessage).addCallback(success -> {
//            // 消息发送到的topic
//            String topic = success.getRecordMetadata().topic();
//            // 消息发送到的分区
//            int partition = success.getRecordMetadata().partition();
//            // 消息在分区内的offset
//            long offset = success.getRecordMetadata().offset();
//            System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
//        }, failure -> {
//            System.out.println("发送消息失败:" + failure.getMessage());
//        });
//    }



}
