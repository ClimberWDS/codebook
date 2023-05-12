package com.wds.codebook.polling.controller;

import com.wds.codebook.polling.event.AppAsyncListener;
import com.wds.codebook.polling.event.EventManager;
import com.wds.codebook.polling.event.HttpEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@RestController
@RequestMapping("/async")
public class AsyncController {

    /**
     * 长轮询接口
     * @param requestName
     * @param request
     * @param response
     */
    @GetMapping("/polling")
    public void getDemo(@RequestParam(value = "requestName") String requestName, HttpServletRequest request, HttpServletResponse response){
        //开启异步支持
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        AsyncContext asyncContext = request.startAsync();
        //添加监听器
        asyncContext.addListener(new AppAsyncListener());
        //设置超时时间
        asyncContext.setTimeout(8000);
        //添加到事件集合中去
        HttpEvent httpEvent = new HttpEvent(requestName, asyncContext);
        EventManager.addHttpEvent(httpEvent);
    }
    /**
     * 触发事件使用
     * @param requestName
     */
    @GetMapping("/trigger")
    public void triggerDemo(@RequestParam(value = "requestName") String requestName){
        EventManager.onEvent(requestName);
    }
}
