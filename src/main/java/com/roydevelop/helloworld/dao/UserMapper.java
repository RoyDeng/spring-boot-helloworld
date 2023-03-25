package com.roydevelop.helloworld.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.Lock;

import com.roydevelop.helloworld.model.User;

import jakarta.persistence.LockModeType;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users(username, email, password, create_time) values (#{username}, #{email}, #{password}, now())")
    public int insert(User user);

    @Update("UPDATE users SET email=#{email}, update_date=now() where id=#{id}")
    int updateByPrimaryKey(User user);

    @Delete("DELETE FROM users WHERE id=#{id}")
    int deleteByPrimaryKey(@Param("id") long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("SELECT * FROM users WHERE id=#{id}")
    User selectByPrimaryKey(@Param("id") long id);

    @Select("SELECT * FROM users WHERE username=#{userName} and password=#{passWord}")
    User selectByUsernameAndPassword(@Param("userName") String username, @Param("passWord") String password);
}
