package com.wds.codebook.kafka.producer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wds.codebook.common.utils.GenerateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wds
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExampleDto {
    private Integer user_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd ")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    private String city;
    private Integer age;
    private Integer sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date last_visit_date;
    private Integer cost;
    private Integer max_dwell_time;
    private Integer min_dwell_time;



    public static ExampleDto genExampleDto() {

        return ExampleDto.builder().user_id(GenerateUtils.generateNum(3))
                .date(GenerateUtils.generateDate())
                .city(GenerateUtils.generateArea())
                .age(GenerateUtils.generateNum(2))
                .sex(GenerateUtils.generateSex())
                .last_visit_date(GenerateUtils.generateDate())
                .cost(GenerateUtils.generateNum(3))
                .max_dwell_time(GenerateUtils.generateNum(2))
                .min_dwell_time(GenerateUtils.generateNum(2))
                .build();
    }
}
