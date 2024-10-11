package com.sinsuren.example;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ClientWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Add session to the set when a new client connects
        sessions.add(session);
        session.sendMessage(new TextMessage("Welcome! You are connected to the server."));
        System.out.println("New client connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Broadcast the received message to all connected clients
        String clientMessage = message.getPayload();
        System.out.println("Received message from client: " + clientMessage);

        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage("Broadcast: " + clientMessage));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // Remove session from the set when a client disconnects
        sessions.remove(session);
        System.out.println("Client disconnected: " + session.getId());
    }
}
