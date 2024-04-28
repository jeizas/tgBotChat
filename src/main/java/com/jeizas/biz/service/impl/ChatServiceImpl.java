package com.jeizas.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.jeizas.biz.dto.ChatDTO;
import com.jeizas.biz.service.ChatService;
import com.jeizas.common.constant.StringConstant;
import com.jeizas.domain.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


/**
 * web聊天工具类
 */
@Slf4j
@Service(value = "ChatService")
public class ChatServiceImpl implements ChatService {

    @Resource
    private SimpMessagingTemplate template;

    private User user;

    public void pushMessage(String chatId, String text) {
        if (user == null) {
            return;
        }
        if (System.getProperty(StringConstant.PRO_KEY_CHAT).equals(chatId)) {
            ChatDTO chatDTO = new ChatDTO();
            if (text.contains(StringConstant.PRO_KEY_NAME)) {
                String[] realText = text.split(StringConstant.PRO_KEY_NAME);
                chatDTO.setContent(realText[1]);
            } else {
                chatDTO.setContent(text);
            }
            chatDTO.setTime(System.currentTimeMillis());
            chatDTO.setUserName("客服");
            template.convertAndSend("/chat/single/" + user.getUuid(), chatDTO);
        }
    }

    public void disconnectMessage(String text) {
        if (user == null) {
            return;
        }
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setContent(text);
        chatDTO.setTime(System.currentTimeMillis());
        chatDTO.setUserName("客服");
        template.convertAndSend("/chat/disconnect/" + user.getUuid(), chatDTO);
        user = null;
    }

    @Override
    public boolean addUser(User curUser) {
        if (user != null) {
            return false;
        }
        this.user = curUser;
        return true;
    }

    @Override
    public void updateUser(Boolean isStart) {
        if (user != null) {
            user.setIsStart(isStart);
        }
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void delUser() {
        log.info("监听连接断开，清空用户，user={}", JSON.toJSONString(user));
        user = null;
    }
}
