package com.roydevelop.helloworld.service;

import java.util.List;

import com.roydevelop.helloworld.model.User;

public interface UserService {
    int insertUser(User user);
    int deleteById(Long id);
    int updateById(User user);
    User selectById(Long id) throws Exception;
    User checkUser(String username, String password);
    List<User> selectAllUsersAsync();
    List<User> selectAllUsers();
}
