package com.roydevelop.helloworld.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.roydevelop.helloworld.config.rabbitmq.RabbitConfig;
import com.roydevelop.helloworld.model.Mail;
import com.roydevelop.helloworld.utils.mail.MailUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MailConsumer {
    @Autowired
    private MailUtil mailUtil;

    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {
        Mail mail = MessageHelper.msgToObj(message, Mail.class);
        log.info("Message received: {}", mail.toString());

        boolean success = mailUtil.send(mail);

        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();

        if (success) {
            channel.basicAck(tag, false);
        } else {
            channel.basicNack(tag, false, true);
        }
    }
}
