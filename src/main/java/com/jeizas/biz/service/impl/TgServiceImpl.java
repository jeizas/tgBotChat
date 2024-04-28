package com.jeizas.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.jeizas.biz.service.ChatService;
import com.jeizas.biz.service.TgService;
import com.jeizas.common.constant.StringConstant;
import com.jeizas.domain.User;
import com.jeizas.infrastructure.listener.TgLongPollingBot;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Tg service.
 */
@Slf4j
@Service(value = "TgService")
public class TgServiceImpl implements TgService {

    @Resource
    private TgLongPollingBot tgLongPollingBot;
    @Resource
    private ChatService chatService;

    @Override
    public void sendText(String chatId, String text, boolean isSystem) {
        User user = chatService.getUser();
        if (user == null) {
            return;
        }
        String content = null;
        if (isSystem) {
            content = "系统：" + text;
        } else {
            StringBuilder str = new StringBuilder();
            str.append("用户名：*").append(user.getUserName()).append("*").append("\n")
                    .append("邮箱：*").append(user.getEmail()).append("*").append("\n")
                    .append("反馈：`").append(text).append("`");
            content = str.toString();
        }
        SendMessage req = SendMessage.builder()
                .chatId(chatId)
                .parseMode("Markdown")
                .text(content)
                .build();
        try {
            Object obj = tgLongPollingBot.execute(req);
            log.info("sendText, msg={}, resp={}", JSON.toJSONString(req), JSON.toJSONString(obj));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendTextHello() {
        User user = chatService.getUser();
        StringBuilder str = new StringBuilder();
        str.append("用户名：*").append(user.getUserName()).append("*").append("\n")
                .append("邮箱：*").append(user.getEmail()).append("*").append("\n")
                .append("进入聊天");

        InlineKeyboardButton button1 = InlineKeyboardButton.builder().text("开始").callbackData("kfStart").build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder().text("拒绝").callbackData("kfCancel").build();
        List<InlineKeyboardButton> list1 = new ArrayList<>();
        Collections.addAll(list1, button1, button2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(list1);

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboard(rowList).build();

        SendMessage req = SendMessage.builder()
                .chatId(System.getProperty(StringConstant.PRO_KEY_CHAT))
                .parseMode("Markdown")
                .text(str.toString())
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        try {
            Object obj = tgLongPollingBot.execute(req);
            log.info("sendText, msg={}, resp={}", JSON.toJSONString(req), JSON.toJSONString(obj));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delMessage(Integer messageId, String chatId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(messageId);
        deleteMessage.setChatId(chatId);
        try {
            tgLongPollingBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
