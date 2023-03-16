package com.roydevelop.helloworld.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.roydevelop.helloworld.model.User;

@Mapper
public interface UserMapper {
    @Insert("insert into user(email,password,create_time) values (#{email},#{password},now())")
    public int insert(User user);

    @Update("update user set email=#{email},password=#{password},update_date=now() where uid=#{uid}")
    int updateByPrimaryKey(User user);

    @Delete("delete from user where uid=#{uid}")
    int deleteByPrimaryKey(@Param("uid")int uid);

    @Select("SELECT * FROM user where email=#{email}")
    List<User> selectByEmail(@Param("email") String email);

    @Select("SELECT * FROM user where uid=#{uid}")
    User selectByPrimaryKey(@Param("uid")int uid);

    @Select("SELECT * FROM user where email=#{email} and password=#{passWord}")
    User selectByEmailAndPassword(@Param("email") String username,@Param("passWord") String password);

    @Select("SELECT * FROM user order by uid desc")
    List<User> selectAllUser();
}
