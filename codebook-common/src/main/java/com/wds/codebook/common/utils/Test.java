package com.wds.codebook.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Test {
    public static void main(String[] args) {


//        LocalDateTime date = LocalDateTime.of(2024,1,29,9,53,03) ;
//        for(int i=1;i<13;i++){
//            System.out.println(calSubscriberOrderExpirationTime(date,i));
//        }

        System.out.println(StringUtils.substring("1320230330urbmSmDn01",19));

    }

    public static Date calSubscriberOrderExpirationTime(Date effectiveTime, Integer buyDuration) {
        return DateUtil.asDate(calSubscriberOrderExpirationTime(DateUtil.asLocalDateTime(effectiveTime),buyDuration));
    }

    public static LocalDateTime calSubscriberOrderExpirationTime(LocalDateTime effectiveTime, Integer buyDuration) {
        int dayLen = 0;
        for(int i=0;i<buyDuration;i++){
            dayLen += effectiveTime.toLocalDate().plusMonths(i).lengthOfMonth();
        }
        System.out.println("相差天数:"+dayLen);
        LocalDateTime res = effectiveTime.plusDays(dayLen);
        return res;
    }


}
