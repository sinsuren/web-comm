package com.sinsuren.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@SpringBootApplication
public class WebSocketServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebSocketServerApplication.class, args);
  }
}

@Configuration
@EnableWebSocket
class WebSocketConfig implements WebSocketConfigurer {

  @Autowired private ClientWebSocketHandler clientWebSocketHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(clientWebSocketHandler, "/client-details").setAllowedOrigins("*");
  }
}
