package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.MenuMapper;
import com.yinxiang.raspberry.mapper.UserMapper;
import com.yinxiang.raspberry.model.Menu;
import com.yinxiang.raspberry.model.Result;
import com.yinxiang.raspberry.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class MenuService {

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    UserMapper userMapper;


    public List<Menu> getMenusByHrId() {
        return menuMapper.getMenusByHrId();
    }

    public List<Menu> getAllMenu() {
        return menuMapper.getAllMenus();
    }


    public Result permission(String username,String[] menunames) {
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        User existUser = userMapper.loadUserByUsername(username);
        if(existUser == null) {
            result.setMsg("用户不存在");
        }else {
            menuMapper.deleteMenuUserByUid(existUser.getId());
            for(int i = 0;i<menunames.length;i++) {
                menuMapper.addMenuUser(menuMapper.getMidByName(menunames[i]),existUser.getId());
            }
            result.setMsg("添加成功");
            result.setSuccess(true);
            result.setStatus(200);
        }
        return result;
    }
}
