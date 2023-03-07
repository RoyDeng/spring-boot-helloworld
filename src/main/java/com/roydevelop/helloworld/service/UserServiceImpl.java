package com.roydevelop.helloworld.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.roydevelop.helloworld.executor.TaskThread;
import com.roydevelop.helloworld.executor.ThreadPoolExecutorUtil;
import com.roydevelop.helloworld.model.User;
import com.roydevelop.helloworld.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ThreadPoolExecutorUtil threadPoolExecutorUtil;

    public UserServiceImpl(UserRepository userRepository, ThreadPoolExecutorUtil threadPoolExecutorUtil) {
        this.userRepository = userRepository;
        this.threadPoolExecutorUtil = threadPoolExecutorUtil;
    }

    @Override
    public List<User> getAllUsersAsync() {
        for (int i = 0; i < 20000; i++) {
            TaskThread taskThread = new TaskThread(userRepository);
            threadPoolExecutorUtil.executeTask(taskThread);
        }

        TaskThread taskThread = new TaskThread(userRepository);
        threadPoolExecutorUtil.executeTask(taskThread);

        return taskThread.users;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
