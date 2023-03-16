package com.roydevelop.helloworld.logback;

import java.nio.charset.StandardCharsets;

import com.roydevelop.helloworld.channel.LogChannel;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WebSocketAppender extends AppenderBase<ILoggingEvent> {
    private PatternLayoutEncoder encoder;

    @Override
    protected void append(ILoggingEvent eventObject) {
        byte[] data = this.encoder.encode(eventObject);
        LogChannel.push(new String(data, StandardCharsets.UTF_8));
    }
}
