package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.UserMapper;
import com.yinxiang.raspberry.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebService {
    @Autowired
    UserMapper userMapper;


    public List<User> getUserByPage(Integer page, Integer size, String keywords, String username, String description,String rolename) {
        int start = (page - 1) * size;
        return userMapper.getUserByPage(start, size, keywords,username,description,rolename);
    }

    public Long getCountByKeywords(String keywords,String username,String description,String editable) {
        return userMapper.getCountByKeywords(keywords,username,description,editable);
    }

    public int updateUser(User user) {
        String password = user.getPassword();
        System.out.println(user);
        if(password != null) {
            password = new BCryptPasswordEncoder().encode(password);
            user.setPassword(password);
        }
        return userMapper.updateUser(user);
    }



}
