//package com.wds.codebook.telegram.common;
//
//
//import com.wds.codebook.telegram.config.TelegramBot;
//import com.wds.codebook.telegram.config.TelegramConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//import javax.annotation.Resource;
//import java.util.Objects;
//
///**
// * telegram 发送消息
// * @author: wds
// * @DateTime: 2024/11/22 10:12
// */
//
//@Slf4j
//@Component
//public class TelegramGetUtil implements InitializingBean {
//
//    private TelegramBot mTelegramBot;
//
//    @Resource
//    TelegramConfig telegramConfig;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//        mTelegramBot = new TelegramBot(telegramConfig.getToken(), telegramConfig.getUsername());
//        telegramBotsApi.registerBot(mTelegramBot);
//        log.debug("TelegramInit afterPropertiesSet start!");
//    }
//
//    /**
//     * @param msg    发生消息
//     * @param charId 群组id
//     * @throws TelegramApiException
//     */
//    public void sendMsg(String msg, Long charId) {
//        if (Objects.isNull(mTelegramBot)) {
//            log.debug("TelegramBot sendMsg bot is null!");
//            return;
//        }
//        mTelegramBot.sendMsg(msg, charId);
//    }
//
//
//
//
//}