package org.example.back;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // /ws/chat 경로로 들어오는 WebSocket 요청을 TextWebSocketHandler로 처리하도록 설정
        registry.addHandler(webSocketHandler(), "/ws/chat")
                .addInterceptors(new HttpSessionHandshakeInterceptor())  // 세션을 핸들링
                .setAllowedOrigins("*");  // CORS 설정
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new TextWebSocketHandler() {
            @Override
            protected void handleTextMessage(org.springframework.web.socket.WebSocketSession session, org.springframework.web.socket.TextMessage message) throws Exception {
                // 클라이언트로부터 받은 메시지를 처리하고 응답을 보냄
                System.out.println("Received message: " + message.getPayload());

                // 예시로 메시지를 그대로 클라이언트에게 돌려보냄
                session.sendMessage(new org.springframework.web.socket.TextMessage("Echo: " + message.getPayload()));
            }
        };
    }
}