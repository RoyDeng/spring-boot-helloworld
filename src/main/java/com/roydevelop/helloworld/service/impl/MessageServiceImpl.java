package com.roydevelop.helloworld.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roydevelop.helloworld.dao.MessageMapper;
import com.roydevelop.helloworld.model.EMessageType;
import com.roydevelop.helloworld.model.Message;
import com.roydevelop.helloworld.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void updateMessageStatus(String id, EMessageType status) {
        messageMapper.updateMessageStatus(id, status);
    }

    @Override
    public Message getMessageById(String id) {
        return messageMapper.getMessageById(id);
    }

    @Override
    public List<Message> getTimeoutMessage() {
        return messageMapper.getTimeoutMessage();
    }

    @Override
    public void updateTryCount(String id, Date tryTime) {
        messageMapper.updateTryCount(id, tryTime);
    }

    @Override
    public void insertMessage(Message message) {
        messageMapper.insertMessage(message);
    }
}
