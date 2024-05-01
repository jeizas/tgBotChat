package com.jeizas.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.jeizas.biz.dto.ChatDTO;
import com.jeizas.biz.service.ChatService;
import com.jeizas.biz.service.TgService;
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

    /**
     * The constant TYPE_UPDATE_START.
     */
    public static final String TYPE_UPDATE_START = "startTime";
    /**
     * The constant TYPE_UPDATE_CHAT_STAMP.
     */
    public static final String TYPE_UPDATE_CHAT_STAMP = "updateTime";

    public static final String TYPE_UPDATE_CONNECT = "connectTime";


    @Resource
    private SimpMessagingTemplate template;
    @Resource
    private TgService tgService;

    private User user;

    @Override
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
            updateUser(TYPE_UPDATE_CHAT_STAMP);
        }
    }

    @Override
    public void disconnectMessage(String text) {
        if (user == null) {
            return;
        }
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setContent(text);
        chatDTO.setTime(System.currentTimeMillis());
        chatDTO.setUserName("客服");
        chatDTO.setDisconnect(true);
        template.convertAndSend("/chat/single/" + user.getUuid(), chatDTO);
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
    public void updateUser(String type) {
        if (user != null) {
            if (TYPE_UPDATE_START.equals(type)) {
                user.setChartStartTime(System.currentTimeMillis());
            }
            if (TYPE_UPDATE_CHAT_STAMP.equals(type)) {
                user.setChatUpdateTime(System.currentTimeMillis());
            }
            if (TYPE_UPDATE_CONNECT.equals(type)) {
                user.setConnectTime(System.currentTimeMillis());
            }
        }
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public synchronized void delUser() {
        log.info("监听连接断开，清空用户，user={}", JSON.toJSONString(user));
        if (user != null) {
            tgService.sendText(System.getProperty(StringConstant.PRO_KEY_CHAT), "_用户：" + user.getUserName() + "_，会话已结束", true);
        }
        user = null;
    }
}
