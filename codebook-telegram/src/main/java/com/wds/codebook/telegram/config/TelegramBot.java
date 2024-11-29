package com.wds.codebook.telegram.config;


import com.wds.codebook.common.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 * telegram机器人初始化
 *
 * @author: wds
 * @DateTime: 2024/11/22 10:13
 */
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private String mToken;
    private String mUsername;

    public TelegramBot(String token, String username, DefaultBotOptions options) {
        super(options);
        mToken = token;
        mUsername = username;
    }

    @Override
    public String getBotUsername() {
        return mUsername;
    }

    @Override
    public String getBotToken() {
        return mToken;
    }

    //回复普通文本消息
    public void sendMsg(String text, Long chatId) {

        if (null == mUsername || null == mToken) {
            log.error("TelegramInit sendMsg no set token or username!");
            return;
        }
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(chatId));
        response.setText(text);
        try {
            execute(response);
        } catch (TelegramApiException e) {
            log.debug("TelegramInit sendMsg error:" + e.getLocalizedMessage());
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        // 检查是否是消息更新
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            log.info("message:" + JacksonUtils.toJson(message));
            Long chatId = message.getChatId();
            String text = message.getText();
            String autoReplyMsg = "自动回复消息";
            if (text.startsWith("/help")) {
                autoReplyMsg = "<1> /help 帮助文档 \n"
                        + "<2> /getUser 获取用户信息 \n"
                        + "<3> /getChatId 获取chatId \n";
            }
            if (text.startsWith("/getChatId")) {
                autoReplyMsg = "ChatId:" + chatId;
            }

            if (text.startsWith("/getUser")) {
                User user = update.getMessage().getFrom();
                autoReplyMsg = JacksonUtils.toJson(user);
            }
            sendMsg(autoReplyMsg, chatId);
        }
    }


}
