package com.jeizas.infrastructure.intecepter;

import com.alibaba.fastjson.JSON;
import com.jeizas.biz.service.ChatService;
import com.jeizas.biz.service.TgService;
import com.jeizas.common.context.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;


/**
 * 消息拦截
 */
@Slf4j
public class SocketChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);//消息头访问器
        if (headerAccessor.getCommand() == null) {
            return;
        }
        String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
        switch (headerAccessor.getCommand()) {
            case CONNECT:
                connect(sessionId, message);
                break;
            case DISCONNECT:
                log.info("disconnect, message={}", JSON.toJSONString(message));
                disconnect(sessionId);
                break;
            case SUBSCRIBE:
                break;

            case UNSUBSCRIBE:
                break;
            default:
                break;
        }
    }


    /**
     * Connect.
     *
     * @param sessionId the session id
     * @param message   the message
     */
    private void connect(String sessionId, Message<?> message) {
        TgService tgService = (TgService) SpringContext.getBean("TgService");
        tgService.sendTextHello();
    }

    /**
     * Disconnect.
     *
     * @param sessionId the session id
     */
    private void disconnect(String sessionId) {
        ChatService chatService = (ChatService) SpringContext.getBean("ChatService");
        chatService.delUser();
    }

}
