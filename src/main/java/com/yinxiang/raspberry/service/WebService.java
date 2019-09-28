package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.LocationMapper;
import com.yinxiang.raspberry.mapper.UserMapper;
import com.yinxiang.raspberry.model.User;
import com.yinxiang.raspberry.model.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    LocationMapper locationMapper;


    public List<User> getUserByPage(Integer page, Integer size, String keywords, String username, String description,String rolename,List<String> areaname,int issuper) {
        int start = (page - 1) * size;
        System.out.println(start + " : " + size);
        return userMapper.getUserByPage(start, size, keywords,username,description,rolename,areaname,issuper);
    }

    public Long getCountByKeywords(String keywords,String username,String description,String editable,List<String> areaname,int issuper) {
        return userMapper.getCountByKeywords(keywords,username,description,editable,areaname,issuper);
    }


    //好像是用来修改密码的。
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
