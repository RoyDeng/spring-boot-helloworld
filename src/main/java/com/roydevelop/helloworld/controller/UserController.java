package com.roydevelop.helloworld.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roydevelop.helloworld.model.User;
import com.roydevelop.helloworld.service.UserService;
import com.roydevelop.helloworld.utils.RedisUsing.BloomFilterUtils;
import com.roydevelop.helloworld.utils.security.MD5;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private BloomFilterUtils bloomFilterUtils;

    @RequestMapping(value = "/getUsers", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<User> getAllUsers() {
        return userService.selectAllUser();
    }

    @RequestMapping(value = "/getUsersAsync", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<User> getAllUsersAsync() {
        return userService.selectAllUserAsync();
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String register(@RequestBody Map map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User();
        user.setEmail((String) map.get("email"));
        user.setPassword(MD5.code((String) map.get("password")));
        user.setEmail((String) map.get("email"));
        int result = userService.insertUser(user);

        if (result > 0) {
            int uId = user.getUid();
            bloomFilterUtils.addBloomFilter(String.valueOf(uId), uId);

            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
            System.out.println("Register result: " + jsonString);
            return "Register successfully!";
        } else {
            return "Register failed!";
        }
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String login(@RequestBody User user) {
        User result = userService.checkUser(user.getEmail(), user.getPassword());
        
        if (result == null) {
            return null;
        } else { 
            return "Login successfully!";
        }
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public String deleteUser(@RequestBody Map map) {
        int id = Integer.parseInt(map.get("uid").toString());
        int result = userService.deleteByUid(id);
        
        if (result > 0){
            return "Delete successfully!";
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/selectUserByUid", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String selectUserUid(@RequestBody Map map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(map.get("uid").toString());
        
        try {
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userService.selectByUid(id));
        
            return jsonString;
        } catch (Exception e) {
            return "The data does not exist in the database.";
        }
    }

    @RequestMapping(value = "/selectUserByEmail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String selectUserByName(@RequestBody Map map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String value = map.get("value").toString();
        String jsonString = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(userService.selectByName(value));
        return jsonString;
    }

    @RequestMapping(value = "/updateUser",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String updateUser(@RequestBody Map map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User();
        user.setUid(Integer.parseInt(map.get("uid").toString()));

        try {
            User oldUser = userService.selectByUid(Integer.parseInt(map.get("uid").toString()));

            if (oldUser.getPassword().equals(MD5.code(map.get("password").toString()))) {
                user.setPassword(map.get("password").toString());
            } else {
                user.setPassword(MD5.code( map.get("password").toString()));
            }

            user.setEmail(map.get("email").toString());
            System.out.println(user);
            int result = userService.updateByUid(user);
            if (result > 0) {
                return "Update successfully!";
            } else {
                return null;
            }
        } catch (Exception e) {
            return "The data does not exist in the database.";
        }
    }
}
