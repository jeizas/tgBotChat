package com.jeizas.infrastructure.intecepter;

import com.jeizas.biz.service.ChatService;
import com.jeizas.biz.service.TgService;
import com.jeizas.common.constant.StringConstant;
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


    private void connect(String sessionId, Message<?> message) {
        TgService tgService = (TgService) SpringContext.getBean("TgService");
        tgService.sendTextHello();
    }

    private void disconnect(String sessionId) {
        TgService tgService = (TgService) SpringContext.getBean("TgService");
        tgService.sendText(System.getProperty(StringConstant.PRO_KEY_CHAT), "用户已断开", true);
        ChatService chatService = (ChatService) SpringContext.getBean("ChatService");
        chatService.delUser();
    }

}
