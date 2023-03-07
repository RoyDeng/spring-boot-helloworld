package com.roydevelop.helloworld.executor;

import java.util.List;

import com.roydevelop.helloworld.model.User;
import com.roydevelop.helloworld.repo.UserRepository;

public class TaskThread implements Runnable {
    private final UserRepository userRepository;
    public List<User> users;

    public TaskThread(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        users=userRepository.findAll();
    }
}
