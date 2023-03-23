package com.roydevelop.helloworld.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.roydevelop.helloworld.model.EMessageType;
import com.roydevelop.helloworld.model.Message;
import com.roydevelop.helloworld.service.MessageService;

@Component
public class ResendMail {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageService messageService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final int MAX_TRY_COUNT = 3;

    @Scheduled(cron = "0 */5 * ? * *")
    public void resend(){
        logger.info("Resending messages started.");

        List<Message> messages = messageService.getTimeoutMessage();

        messages.forEach(message -> {
            String id = message.getId();

            if (message.getTryCount() >= MAX_TRY_COUNT) {
                messageService.updateMessageStatus(id, EMessageType.TYPE_DELIVER_FAIL);
                logger.info("This message failed for exceeding the maximum number of retries: {}", id);
            } else {
                messageService.updateTryCount(id, message.getNextTryTime());

                CorrelationData correlationData = new CorrelationData(String.valueOf(id));
                rabbitTemplate.convertAndSend(message.getExchange(), message.getRoutingKey(), MessageHelper.objToMsg(message.getContent()), correlationData);
            }
        });

        logger.info("Resending messages ended.");
    }
}
