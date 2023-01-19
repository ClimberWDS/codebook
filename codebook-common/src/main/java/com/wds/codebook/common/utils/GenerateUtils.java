package com.wds.codebook.common.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author wds
 */
@Component
public class GenerateUtils {
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    static Random random = new Random();

    public static String generateString(int len) {

        //生成指定length的随机字符串（A-Z，a-z，0-9）
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < len; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return String.valueOf(sb);
    }

    public static Integer generateNum(int len) {

        //生成指定length的随机字符串（A-Z，a-z，0-9）
        String str = "123456789";
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < len; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return Integer.valueOf(String.valueOf(sb));
    }


    public static Date generateDate() {

        String beginDate="2021-01-01 00:00:00 ";
        String endDate= format.format(new Date());
        return randomDate(beginDate,endDate);
    }

    public static String generateArea(){
        String[] cityArr = {"北京","广东","山东","江苏","河南","上海","河北","浙江","香港","山西","陕西","湖南","重庆","福建","天津","云南","四川","广西","安徽","海南","江西","湖北","山西","辽宁","内蒙古"};
        return generateData(cityArr);
    }

    public static Integer generateSex(){
        int[] cityArr = {0,1};
        return generateData(cityArr);
    }

    public static String generateData(String[] arr){
        int i = random.nextInt(10000)%arr.length;
//        System.out.println(arr[i]);
        return arr[i];
    }
    public static int generateData(int[] arr){
        int i = random.nextInt(10000)%arr.length;
//        System.out.println(arr[i]);
        return arr[i];
    }

    public static void main(String[] args) {
        for (int i=0;i<10;i++){

            generateArea();
        }
    }

    private static Date randomDate(String beginDate, String endDate) {
        try {
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);
            //getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    private static long random(long begin,long end){
        long rtn = begin + (long) (Math.random() * (end - begin));
        //如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }




    }
