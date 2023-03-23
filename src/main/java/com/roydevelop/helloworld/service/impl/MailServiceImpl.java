package com.roydevelop.helloworld.service.impl;

import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roydevelop.helloworld.config.rabbitmq.RabbitConfig;
import com.roydevelop.helloworld.consumer.MessageHelper;
import com.roydevelop.helloworld.model.Mail;
import com.roydevelop.helloworld.model.Message;
import com.roydevelop.helloworld.service.MailService;
import com.roydevelop.helloworld.service.MessageService;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MessageService messageService;

    @Override
    public void send(Mail mail) {
        String id = UUID.randomUUID().toString();
        mail.setMsgId(id);

        Message message = new Message(mail.getContent(), RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME);
        messageService.insertMessage(message);

        CorrelationData correlationData = new CorrelationData(id);

        rabbitTemplate.convertAndSend(
            RabbitConfig.MAIL_EXCHANGE_NAME,
            RabbitConfig.MAIL_ROUTING_KEY_NAME,
            MessageHelper.objToMsg(mail),
            correlationData
        );
    }
}
