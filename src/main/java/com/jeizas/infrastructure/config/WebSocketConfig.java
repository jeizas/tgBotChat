package com.jeizas.infrastructure.config;

import com.jeizas.biz.service.ChatService;
import com.jeizas.infrastructure.intecepter.HttpHandShakeInterceptor;
import com.jeizas.infrastructure.intecepter.SocketChannelInterceptor;
import com.jeizas.infrastructure.listener.WebSocketEventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * websocket配置类
 *
 * @author jeizas
 */
@Configuration
@ConditionalOnBean(value = ChatService.class)
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 配置基站
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
                .addInterceptors(new HttpHandShakeInterceptor())
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * 配置消息代理(中介)
     * enableSimpleBroker 服务端推送给客户端的路径前缀
     * setApplicationDestinationPrefixes  客户端发送数据给服务器端的一个前缀
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/topic", "/chat");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");  // 设置用户特定目的地前缀;
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new SocketChannelInterceptor());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(new SocketChannelInterceptor());
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setSendTimeLimit(15 * 1000)
                .setSendBufferSizeLimit(512 * 1024)
                .setMessageSizeLimit(16 * 1024)
                .setTimeToFirstMessage(60);
    }

    /**
     * Web socket event listener web socket event listener.
     *
     * @return the web socket event listener
     */
    @Bean
    public WebSocketEventListener webSocketEventListener() {
        return new WebSocketEventListener();
    }


}
