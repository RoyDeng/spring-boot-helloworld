package com.roydevelop.helloworld.utils.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.roydevelop.helloworld.model.EMessageType;
import com.roydevelop.helloworld.model.Mail;
import com.roydevelop.helloworld.service.MessageService;

@Component
public class MailUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("$spring.mail.properties.mail.smtp.from")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageService messageService;

    public boolean send(Mail mail) {
        String to = mail.getTo();
        String title = mail.getTitle();
        String content = mail.getContent();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(title);
        message.setText(content);
        message.setTo(to);

        try {
            mailSender.send(message);
            messageService.updateMessageStatus(mail.getMsgId(), EMessageType.TYPE_DELIVER_SUCCESS);
            return true;
        } catch (MailException e) {
            logger.error("Failed to send the mail titled {} to {}: {}", title, to, e);
            messageService.updateMessageStatus(mail.getMsgId(), EMessageType.TYPE_DELIVER_FAIL);
            return false;
        }
    }
}
