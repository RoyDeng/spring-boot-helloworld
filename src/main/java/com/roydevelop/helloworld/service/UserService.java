package com.roydevelop.helloworld.service;

import java.util.List;

import com.roydevelop.helloworld.model.User;

public interface UserService {
    List<User> getAllUsersAsync();
    List<User> getAllUsers();
}
