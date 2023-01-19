package com.wds.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Receiver {
    @StreamListener(Sink.INPUT)
    public void process(Object message){

      log.info("receiver message:"+message);
    }
}
