package com.wds.codebook.kafka.consumer;

import org.springframework.stereotype.Component;

import java.util.Optional;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@Component
public class Consumer {

    @KafkaListener(topics = {"test"})
    public void listen1(ConsumerRecord<?, ?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            System.out.println("---->"+record);
            System.out.println("---->"+message);
        }
    }



    @KafkaListener(topics = {"test-doris-topic1"})
    public void listenDoris(ConsumerRecord<?, ?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            System.out.println("---->"+record);
            System.out.println("---->"+message);
        }
    }
}
