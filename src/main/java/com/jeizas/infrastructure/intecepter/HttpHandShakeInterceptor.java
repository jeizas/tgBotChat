package com.jeizas.infrastructure.intecepter;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket握手请求的拦截器
 */
public class HttpHandShakeInterceptor implements HandshakeInterceptor{

	/**
	 *
	 * 在握手之前执行该方法, 继续握手返回true, 中断握手返回false
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {

		if(request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
			HttpSession session =  servletRequest.getServletRequest().getSession();
			String sessionId = session.getId();
			//这里将sessionId放入SessionAttributes中，
			attributes.put("sessionId", sessionId);
		}
		return true;
	}


	/**
	 * 在握手之后执行该方法
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {

		if(request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
			HttpSession session =  servletRequest.getServletRequest().getSession();
			String sessionId = session.getId();
		}
	}
}
