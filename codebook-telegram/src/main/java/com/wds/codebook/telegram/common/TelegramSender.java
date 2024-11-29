package com.wds.codebook.telegram.common;


import com.wds.codebook.telegram.config.TelegramBot;
import com.wds.codebook.telegram.config.TelegramConfig;
import com.wds.codebook.telegram.config.TelegramProxyConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * telegram 发送消息
 * @author: wds
 * @DateTime: 2024/11/22 10:12
 */

@Slf4j
@Component
public class TelegramSender implements InitializingBean {

    private TelegramBot mTelegramBot;

    @Resource
    TelegramConfig telegramConfig;

    @Resource
    TelegramProxyConfig telegramProxyConfig;

    @Override
    public void afterPropertiesSet() throws Exception {


        // 配置代理
        DefaultBotOptions botOptions = new DefaultBotOptions();

        // 设置代理类型和地址
        botOptions.setProxyHost(telegramProxyConfig.getHost()); // 替换为您的代理地址
        botOptions.setProxyPort(telegramProxyConfig.getPort());        // 替换为您的代理端口
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5); // 支持 HTTP 或 SOCKS5

        // 如果需要身份验证，设置全局代理认证
//        Authenticator.setDefault(new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(telegramProxyConfig.getUserName(), telegramProxyConfig.getPassword().toCharArray());
//            }
//        });

        try {
            log.debug("TelegramInit afterPropertiesSet start!");
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            mTelegramBot = new TelegramBot(telegramConfig.getToken(), telegramConfig.getUsername(),botOptions);
            telegramBotsApi.registerBot(mTelegramBot);
            log.debug("TelegramInit afterPropertiesSet start!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param msg    发生消息
     * @param charId 群组id
     * @throws TelegramApiException
     */
    public void sendMsg(String msg, Long charId) {
        if (Objects.isNull(mTelegramBot)) {
            log.debug("TelegramBot sendMsg bot is null!");
            return;
        }
        mTelegramBot.sendMsg(msg, charId);
    }




}