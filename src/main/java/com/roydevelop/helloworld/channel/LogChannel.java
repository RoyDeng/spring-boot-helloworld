package com.roydevelop.helloworld.channel;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.CloseReason.CloseCodes;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

@ServerEndpoint(value = "/channel/log")
@Slf4j
public class LogChannel {
    public static final ConcurrentMap<String, LogChannel> CHANNELS = new ConcurrentHashMap<>();

    private Session session;

    @OnMessage(maxMessageSize = 1)
    public void onMessage(String message) {
        log.debug("Recv Message: {}", message);
        
        try {
            this.session.close(new CloseReason(CloseCodes.TOO_BIG, "This endpoint does not accept client messages"));
        } catch (IOException e) {
            log.error("Connection close error: id={}, err={}", this.session.getId(), e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        this.session.setMaxIdleTimeout(0);
        CHANNELS.put(this.session.getId(), this);
        
        log.info("New client connection: id={}", this.session.getId());
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
        log.info("Connection disconnected: id={}, err={}", this.session.getId(), closeReason);

        CHANNELS.remove(this.session.getId());
    }

    @OnError
    public void onError(Throwable throwable) throws IOException {
        log.info("Connection Error: id={}, err={}", this.session.getId(), throwable);
        this.session.close(new CloseReason(CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    }

    public static void push(Object message) {
        CHANNELS.values().stream().forEach(endpoint -> {
            if (endpoint.session.isOpen()) {
                endpoint.session.getAsyncRemote().sendObject(message);
            }
        });
    }
}
