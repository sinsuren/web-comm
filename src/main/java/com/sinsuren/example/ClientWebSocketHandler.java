package com.sinsuren.example;

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

public class ClientWebSocketHandler extends TextWebSocketHandler {

    // Map to store clientId and associated WebSocket session
    private final Map<String, WebSocketSession> clientSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // No need to add the session until we receive a clientId
        session.sendMessage(new TextMessage("Please send your clientId to register."));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload();

        // Assuming the first message from client contains "clientId:<ID>"
        if (clientMessage.startsWith("clientId:")) {
            String clientId = clientMessage.split(":")[1];
            // Register client session
            clientSessions.put(clientId, session);
            System.out.println("Client registered with ID: " + clientId);
            session.sendMessage(new TextMessage("You are registered with clientId: " + clientId));
        } else {
            // Assuming other messages contain "clientId:<ID> Message:<message>"
            String[] parts = clientMessage.split(" ", 2);
            if (parts.length == 2) {
                String clientId = parts[0].split(":")[1]; // Extract the clientId from the message
                String userMessage = parts[1];            // The actual message

                // Find the session for the target client
                WebSocketSession targetSession = clientSessions.get(clientId);
                if (targetSession != null && targetSession.isOpen()) {
                    targetSession.sendMessage(new TextMessage("Message for you: " + userMessage));
                } else {
                    session.sendMessage(new TextMessage("Client ID " + clientId + " not found or session is closed."));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // Clean up the session for the disconnected client
        clientSessions.values().remove(session);
        System.out.println("Client disconnected: " + session.getId());
    }
}
