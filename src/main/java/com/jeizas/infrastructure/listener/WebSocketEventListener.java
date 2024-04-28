package com.jeizas.infrastructure.listener;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

/**
 * The type Web socket event listener.
 */
public class WebSocketEventListener {

    /**
     * Handle web socket connect listener.
     *
     * @param event the event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        System.out.println("Received a new web socket connection with session id: " + sessionId);
        // 在这里可以进行一些业务逻辑处理，例如记录用户连接
    }
}
