package com.jeizas.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.jeizas.biz.service.ChatService;
import com.jeizas.biz.service.TgService;
import com.jeizas.common.constant.StringConstant;
import com.jeizas.common.context.SpringContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * tg消息
 */
@Slf4j
@Data
public class TgMessageUpdate {
    /**
     * id
     */
    @JSONField(name = "update_id")
    private String updateId;

    /**
     * 消息
     */
    private Message message;

    /**
     * The type Message.
     */
    @Data
    public static class Message {
        private User chat;
        private User from;
        private Long date;
        @JSONField(name = "message_id")
        private String messageId;
        private String text;
    }

    /**
     * The type User.
     */
    @Data
    public static class User {

        @JSONField(name = "first_name")
        private String firstName;
        private String id;
        @JSONField(name = "is_bot")
        private Boolean isBot;
        @JSONField(name = "is_premium")
        private Boolean isPremium;
        @JSONField(name = "language_code")
        private String languageCode;
        @JSONField(name = "last_name")
        private String lastName;
        private String username;
        private String type;
    }

    /**
     * 自动回复消息
     *
     * @param update the update
     */
    public static void receiveMsg(Update update) {
        ChatService chatService = (ChatService) SpringContext.getBean("ChatService");
        TgService tgService = (TgService) SpringContext.getBean("TgService");
        CallbackQuery callbackQuery = update.getCallbackQuery();
        // 按钮回复
        if (callbackQuery != null) {
            String chatId = String.valueOf(callbackQuery.getMessage().getChatId());
            Integer messageId = callbackQuery.getMessage().getMessageId();
            String command = callbackQuery.getData();
            switch (command) {
                case "kfStart":
                    chatService.pushMessage(chatId, "客服已接入，聊天开始");
                    chatService.updateUser(true);
                    tgService.sendText(chatId, "开始聊天，回复用户，请@机器人", true);
                    break;
                case "kfCancel":
                    chatService.disconnectMessage("客服忙，请稍后重新连接...");
                    break;
                case "kfClose":
                    chatService.disconnectMessage("客服繁忙，请稍后再试");
                    break;
            }
            tgService.delMessage(messageId, chatId);
            return;
        }
        org.telegram.telegrambots.meta.api.objects.Message msg = update.getMessage();
        if (msg != null) {
            boolean isCommand = msg.getEntities() != null
                    && msg.getEntities().stream().anyMatch(i -> "bot_command".equals(i.getType()));
            if (isCommand) {
                String command = msg.getText()
                        .replaceAll(System.getProperty(StringConstant.PRO_KEY_NAME), "")
                        .replaceAll("\\s", "");
                switch (command) {
                    case "/kfClose":
                        chatService.disconnectMessage("客服繁忙，请稍后再试");
                        break;
                }
            } else {
                if (!msg.getFrom().getIsBot()) {
                    if (msg.getText().contains(System.getProperty(StringConstant.PRO_KEY_NAME))) {
                        String text = msg.getText().replaceAll(System.getProperty(StringConstant.PRO_KEY_NAME), "");
                        chatService.pushMessage(String.valueOf(msg.getChat().getId()), text);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
