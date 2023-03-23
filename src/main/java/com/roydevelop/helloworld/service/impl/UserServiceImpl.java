package com.roydevelop.helloworld.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.roydevelop.helloworld.annotation.Cacheable;
import com.roydevelop.helloworld.dao.UserMapper;
import com.roydevelop.helloworld.executor.TaskThread;
import com.roydevelop.helloworld.executor.ThreadPoolExecutorUtil;
import com.roydevelop.helloworld.model.User;
import com.roydevelop.helloworld.repo.UserRepository;
import com.roydevelop.helloworld.service.UserService;
import com.roydevelop.helloworld.utils.redisusing.BloomFilterUtils;

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

    @Autowired
    PasswordEncoder encoder;

    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int deleteById(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(User user) {
        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public User selectById(Long id) throws Exception {
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
    @Cacheable(cacheName = "user", key = "#id", capacity = 3)
    public User checkUser(String username, String password) {
        return userMapper.selectByUsernameAndPassword(username, encoder.encode(password));
    }

    @Override
    public List<User> selectAllUsersAsync() {
        for (int i = 0; i < 20000; i++) {
            TaskThread taskThread = new TaskThread(userRepository);
            threadPoolExecutorUtil.executeTask(taskThread);
        }

        TaskThread taskThread = new TaskThread(userRepository);
        threadPoolExecutorUtil.executeTask(taskThread);

        return taskThread.users;
    }

    @Override
    public List<User> selectAllUsers() {
        int pagNbr = 0;
        Slice<User> userSlice;
        List<User> usertList = new ArrayList<>();

        do {
            PageRequest pageRequest = PageRequest.of(pagNbr, 50);
            userSlice = userRepository.findAllBySlice(pageRequest);
            pagNbr += 1;

            if (userSlice != null) {
                usertList.addAll(userSlice.stream().collect(Collectors.toList()));
            }
        } while (userSlice != null && userSlice.hasNext());

        return usertList;
    }
}
