package com.sinsuren.example;

import org.springframework.stereotype.Service;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.util.Map;

@Service
public class RedisMessageSubscriber implements MessageListener {

    private final Map<String, WebSocketSession> clientSessions;

    public RedisMessageSubscriber(Map<String, WebSocketSession> clientSessions) {
        this.clientSessions = clientSessions;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        String[] parts = messageBody.split(" ", 2);

        if (parts.length == 2) {
            String clientId = parts[0].split(":")[1];  // Extract clientId
            String payload = parts[1];                 // Message content

            // Check if clientId is connected to this server
            WebSocketSession session = clientSessions.get(clientId);
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(payload));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
