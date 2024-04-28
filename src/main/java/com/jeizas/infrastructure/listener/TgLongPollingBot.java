package com.jeizas.infrastructure.listener;

import com.alibaba.fastjson.JSON;
import com.jeizas.domain.TgMessageUpdate;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * 机器人消息处理类
 */
@Slf4j
public class TgLongPollingBot extends TelegramLongPollingBot {

    private final String botName;

    /**
     * Instantiates a new Tg long polling bot.
     *
     * @param botToken the bot token
     * @param botName  the bot name
     */
    public TgLongPollingBot(String botToken, String botName) {
        super(botToken);
        this.botName = botName;
    }

    /**
     * Method for receiving messages.
     *
     * @param update Contains a message from the user.
     */
    @Override
    public void onUpdateReceived(Update update) {
        log.info("监听到消息，update={}", JSON.toJSONString(update));
        TgMessageUpdate.receiveMsg(update);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}