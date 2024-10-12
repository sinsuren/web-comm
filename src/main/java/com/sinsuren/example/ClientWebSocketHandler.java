package com.sinsuren.example;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClientWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> clientSessions = new ConcurrentHashMap<>();
    private final RedisTemplate<String, String> redisTemplate;
    private final ChannelTopic topic;

    public ClientWebSocketHandler(RedisTemplate<String, String> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("Please send your clientId to register."));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload();

        if (clientMessage.startsWith("clientId:")) {
            String clientId = clientMessage.split(":")[1];
            clientSessions.put(clientId, session);
            System.out.println("Client registered with ID: " + clientId);
            session.sendMessage(new TextMessage("You are registered with clientId: " + clientId));
        } else {
            String[] parts = clientMessage.split(" ", 2);
            if (parts.length == 2) {
                String targetClientId = parts[0].split(":")[1];
                String userMessage = parts[1];

                // Publish message to Redis so all servers can receive it
                redisTemplate.convertAndSend(topic.getTopic(), "clientId:" + targetClientId + " " + userMessage);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        clientSessions.values().remove(session);
        System.out.println("Client disconnected: " + session.getId());
    }
}





