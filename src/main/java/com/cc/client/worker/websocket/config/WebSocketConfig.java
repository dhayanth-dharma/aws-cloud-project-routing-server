package com.cc.client.worker.websocket.config;
import org.apache.tomcat.util.net.SocketBufferHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new SocketTextHandler(), "/user").setAllowedOrigins("*");
		registry.addHandler(new SocketTextHandler(), "/potting_initiate_connect").setAllowedOrigins("*");
	}

}