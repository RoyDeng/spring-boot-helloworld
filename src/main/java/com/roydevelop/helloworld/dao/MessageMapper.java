package com.roydevelop.helloworld.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.Lock;

import com.roydevelop.helloworld.model.EMessageType;
import com.roydevelop.helloworld.model.Message;

@Mapper
public interface MessageMapper {
    @Update("UPDATE message_logs SET status=#{status}, update_time=now() WHERE id=#{id}")
    void updateMessageStatus(String id, EMessageType status);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("SELECT id, content, exchange, routing_key, status, try_count, next_try_time, create_time, update_time FROM message_logs WHERE id = #{id}")
    Message getMessageById(String id);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Select("SELECT id, content, exchange, routing_key, status, try_count, next_try_time, create_time, update_time ROM message_logs WHERE status=2")
    List<Message> getTimeoutMessage();

    @Update("UPDATE message_logs SET try_count = try_count + 1, next_try_time=#{tryTime}, update_time=now() WHERE id = #{id}")
    void updateTryCount(String id, Date tryTime);

    @Insert("INSERT INTO message_logs(id, content, exchange, routing_key) value (#{id}, #{content}, #{exchange}, #{routingKey})")
    void insertMessage(Message message);
}
