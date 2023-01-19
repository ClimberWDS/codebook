package com.wds.codebook.kafka;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.wds.codebook.kafka.producer.ExampleDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@SpringBootTest
public class testSend {


    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Test
    void sendDoris(){
        Integer num =1001000;
        num= num==null||num<0?100:num;
        for(int i=1000;i<num;i++){

            ExampleDto exampleDto = ExampleDto.genExampleDto();
            exampleDto.setUser_id(i);
//            log.info("msg:{}",exampleDto.toString());
            kafkaTemplate.send("test-doris-topic", JacksonUtils.toJson(exampleDto));
        }
    }


    @Test
    void testSubstring(){
        byte[] bytes = new byte[]{-15, 14, -68, 90, -82, 80, -55, -82, -121, 11, -69, -4, 51, 36, -93, 85};
        String str = new String(bytes);
        System.out.println(str);
        byte[] byte2 = str.getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(byte2));


        String str2 ="mzh";
        byte[] b2 = str2.getBytes(StandardCharsets.UTF_8);
        String str3 = new String(b2);
        System.out.println(Arrays.toString(b2));
        System.out.println(str3);


        byte[] b3 = new byte[]{109, 122, 104};
        String str4 = new String(b3);
        System.out.println(str4);
        byte[] b4 = str4.getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(b4));


    }

}
