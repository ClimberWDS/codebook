package com.wds.codebook.polling.event;

import javax.servlet.AsyncContext;

/**
 * 
 * @author wds
 * @date 2023-02-13 14:28
 **/
public class HttpEvent {
    /**
     * 可以是业务数据主键，这里用请求名称做个简单demo
     */
    private String requestName;
    private AsyncContext asyncContext;
    public HttpEvent(String requestName,AsyncContext asyncContext){
        this.requestName = requestName;
        this.asyncContext = asyncContext;
    }
    public String getRequestName() {
        return requestName;
    }
    public AsyncContext getAsyncContext() {
        return asyncContext;
    }
}