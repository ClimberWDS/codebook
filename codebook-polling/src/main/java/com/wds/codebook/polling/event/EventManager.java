package com.wds.codebook.polling.event;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
public class EventManager {
    private final static Map<String,HttpEvent> subHttpEvents = new HashMap<>();
    /**
     * 新增事件订阅
     * @param event
     */
    public static void addHttpEvent(HttpEvent event){
        subHttpEvents.put(event.getRequestName(),event);
    }
    /**
     * 触发事件
     * @param requestName
     */
    public static void onEvent(String requestName){
        HttpEvent httpEvent = subHttpEvents.get(requestName);
        if(httpEvent==null){
            return;
        }
        AsyncContext asyncContext = httpEvent.getAsyncContext();
        try {
            PrintWriter writer = asyncContext.getResponse().getWriter();
            writer.print(requestName+" request success!");
            writer.flush();
            asyncContext.complete();
            subHttpEvents.remove(requestName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
