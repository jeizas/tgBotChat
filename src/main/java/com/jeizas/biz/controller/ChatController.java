package com.jeizas.biz.controller;

import com.alibaba.fastjson.JSON;
import com.jeizas.biz.dto.ChatDTO;
import com.jeizas.biz.service.ChatService;
import com.jeizas.biz.service.TgService;
import com.jeizas.common.constant.StringConstant;
import com.jeizas.domain.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


/**
 * 发送socket消息
 */
@Slf4j
@Controller
public class ChatController {

    @Resource
    private ChatService chatService;
    @Resource
    private TgService tgService;

    /**
     * Process message from user.
     *
     * @param message the message
     */
    @MessageMapping("/message")
    public void processMessageFromUser(String message) {
        ChatDTO chatDTO = JSON.parseObject(message, ChatDTO.class);
        User user = chatService.getUser();
        log.info("message={},user={}", JSON.toJSONString(chatDTO), JSON.toJSONString(user));
        if (user == null) {
            return;
        }
        // 客服点击开始，则聊天，如果客服没响应，则等待
        if (user.getChartStartTime() != null) {
            tgService.sendText(System.getProperty(StringConstant.PRO_KEY_CHAT), chatDTO.getContent(), false);
        } else {
            chatService.pushMessage(System.getProperty(StringConstant.PRO_KEY_CHAT), "等待客服中，请稍后...");
        }
    }
}