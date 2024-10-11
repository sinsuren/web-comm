package com.sinsuren.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ClientWebSocket extends WebSocketClient {

    private final String clientId;

    public ClientWebSocket(URI serverUri, String clientId) {
        super(serverUri);
        this.clientId = clientId;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
        // Send clientId to the server for registration
        send("clientId:" + clientId);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message from server: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public static void main(String[] args) {
        try {
            ClientWebSocket client1 = new ClientWebSocket(new URI("ws://localhost:8080/client-details"), "client1");
            client1.connect();

            ClientWebSocket client2 = new ClientWebSocket(new URI("ws://localhost:8080/client-details"), "client2");
            client2.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
