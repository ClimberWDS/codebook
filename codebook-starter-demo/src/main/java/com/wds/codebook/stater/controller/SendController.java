package com.wds.codebook.stater.controller;

import cn.net.chaken.dms.dto.ReportInfo;
import cn.net.chaken.dms.service.ReportClient;
import cn.net.chaken.dms.service.impl.ReportClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author wds
 */
@RestController
@Slf4j
public class SendController {

    @Autowired(required = false)
    public ReportClient reportClient;

    @GetMapping("/sendReport")
    public String sendReport(String reportType,String sourceType){
        ReportInfo reportInfo =  new ReportInfo();
//        reportInfo.setReportType("10");
        reportInfo.setReportType(reportType);
        reportInfo.setSourceType(sourceType);
        reportInfo.setNum(1);
        reportInfo.setMsg("闪信发送抽样数据上报");
        reportInfo.setState(2);
        reportInfo.setIssample(1);
        try {
           //执行业务
        }catch(Exception e){

            reportInfo.setEx(e);
        }
        reportClient.report(reportInfo);
        return "{\"code\":0}";
    }

    @GetMapping("/sendBatch")
    public String sendBatch(String reportType,String sourceType,Integer count){
        count =  Objects.isNull(count)?10:count;
        for(int i =0;i<count;i++){
            send(reportType,sourceType);
        }
        return "{\"code\":0}";
    }


    public void send(String reportType,String sourceType){
        ReportInfo reportInfo =  new ReportInfo();
        reportInfo.setReportType(reportType);
        reportInfo.setSourceType(sourceType);
        reportInfo.setNum(1);
        reportInfo.setMsg("The flashSM_delivery failed");
        reportInfo.setState(2);
        reportInfo.setIssample(1);
        try {
            //执行业务
        }catch(Exception e){

            reportInfo.setEx(e);
        }
        reportClient.report(reportInfo);
    }

}
