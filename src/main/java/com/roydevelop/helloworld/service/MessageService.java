package com.roydevelop.helloworld.service;

import java.util.Date;
import java.util.List;

import com.roydevelop.helloworld.model.EMessageType;
import com.roydevelop.helloworld.model.Message;

public interface MessageService {
    void updateMessageStatus(String id, EMessageType status);
    Message getMessageById(String id);
    List<Message> getTimeoutMessage();
    void updateTryCount(String id, Date tryTime);
    void insertMessage(Message message);
}
