package com.wds.codebook.polling.event;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.PrintWriter;
@WebListener
public class AppAsyncListener implements AsyncListener {
    @Override
    public void onComplete(AsyncEvent asyncEvent) throws IOException {
        System.out.println("AppAsyncListener onComplete");
        // we can do resource cleanup activity here
    }
    @Override
    public void onError(AsyncEvent asyncEvent) throws IOException {
        System.out.println("AppAsyncListener onError");
        //we can return error response to client
    }
    @Override
    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
        System.out.println("AppAsyncListener onStartAsync");
        //we can log the event here
    }
    /**
     * 超时触发
     * @param asyncEvent
     * @throws IOException
     */
    @Override
    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
        AsyncContext asyncContext = asyncEvent.getAsyncContext();
        ServletResponse response = asyncEvent.getAsyncContext().getResponse();
        PrintWriter out = response.getWriter();
        //返回code码，以便前端识别，并重建请求
        out.write(201+" longPolling timeout");
        out.flush();
        asyncContext.complete();
    }
}