package com.sinsuren.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Scanner;

public class ClientWebSocket extends WebSocketClient {

    private final String clientId;

    public ClientWebSocket(URI serverUri, String clientId) {
        super(serverUri);
        this.clientId = clientId;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server as " + clientId);
        send("clientId:" + clientId);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void startMessaging() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter messages in format 'clientId:<targetClientId> <message>'");
        while (true) {
            String input = scanner.nextLine();
            send(input);
        }
    }

    public static void main(String[] args) {
        try {
            String clientId = args.length > 0 ? args[0] : "client1";
            ClientWebSocket client = new ClientWebSocket(new URI("ws://localhost:8080/client-details"), clientId);
            client.connectBlocking();
            client.startMessaging();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
