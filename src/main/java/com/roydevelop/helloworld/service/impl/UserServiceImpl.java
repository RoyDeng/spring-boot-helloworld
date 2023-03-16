package com.roydevelop.helloworld.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.roydevelop.helloworld.annotation.Cacheable;
import com.roydevelop.helloworld.dao.UserMapper;
import com.roydevelop.helloworld.executor.TaskThread;
import com.roydevelop.helloworld.executor.ThreadPoolExecutorUtil;
import com.roydevelop.helloworld.model.User;
import com.roydevelop.helloworld.repo.UserRepository;
import com.roydevelop.helloworld.service.UserService;
import com.roydevelop.helloworld.utils.RedisUsing.BloomFilterUtils;
import com.roydevelop.helloworld.utils.security.MD5;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ThreadPoolExecutorUtil threadPoolExecutorUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BloomFilterUtils bloomFilterUtils;

    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int deleteByUid(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateByUid(User user) {
        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public User selectByUid(Integer id) throws Exception {
        boolean hasKey = bloomFilterUtils.checkBloomFilter(String.valueOf(id), id);

        if (!hasKey) {
            throw new Exception("The data does not exist in the database.");
        } else {
            User user = null;

            if (redisTemplate.hasKey(id + "")) {
                user = (User) redisTemplate.opsForValue().get(id + "");
            } else {
                user = userMapper.selectByPrimaryKey(id);
                redisTemplate.opsForValue().set(id + "", user);
                System.out.println("Generate data cache: " + id);
            }
            return user;
        }
    }

    @Override
    public List<User> selectByName(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    @Cacheable(cacheName = "user", key = "#uid", capacity = 3)
    public User checkUser(String email, String password) {
        return userMapper.selectByEmailAndPassword(email, MD5.code(password));
    }

    @Override
    public List<User> selectAllUserAsync() {
        for (int i = 0; i < 20000; i++) {
            TaskThread taskThread = new TaskThread(userRepository);
            threadPoolExecutorUtil.executeTask(taskThread);
        }

        TaskThread taskThread = new TaskThread(userRepository);
        threadPoolExecutorUtil.executeTask(taskThread);

        return taskThread.users;
    }

    @Override
    public List<User> selectAllUser() {
        return userRepository.findAll();
    }
}
