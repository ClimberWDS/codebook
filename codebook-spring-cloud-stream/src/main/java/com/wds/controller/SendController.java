package com.wds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    private Source source;

    @Autowired
    private StreamBridge streamBridge;



    @GetMapping("sendv1")
    public String  sendv1(String message){
        MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(message);
        source.output().send(messageBuilder.build());
        return "message sended:"+message;
    }

//    @GetMapping("send")
//    public String  send(String message){
//       streamBridge.send("out",message);
//        return "message sended:"+message;
//    }


}
