package com.roydevelop.helloworld.service;

import java.util.List;

import com.roydevelop.helloworld.model.User;

public interface UserService {
    int insertUser(User user);
    int deleteByUid(Integer id);
    int updateByUid(User user);
    User selectByUid(Integer id) throws Exception;
    List<User> selectByName(String userName);
    User checkUser(String username,String password);
    List<User> selectAllUserAsync();
    List<User> selectAllUser();
}
