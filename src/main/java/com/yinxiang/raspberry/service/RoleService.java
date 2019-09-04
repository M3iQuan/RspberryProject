package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.RoleMapper;
import com.yinxiang.raspberry.mapper.UserMapper;
import com.yinxiang.raspberry.mapper.UserRoleMapper;
import com.yinxiang.raspberry.model.Result;
import com.yinxiang.raspberry.model.Role;
import com.yinxiang.raspberry.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    public List<Role> getAllRole() {
        return roleMapper.getAllRole();
    }

    public Result addRole(User user, String[] rolename) {   //修改角色的
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        System.out.println("username:"+user.getUsername());
        System.out.println("user:"+user);
        try {
            User existUser = userMapper.loadUserByUsername(user.getUsername());
            if(existUser == null) {
                result.setMsg("用户不存在");
            }else {
                userRoleMapper.deleteRoleByUid(existUser.getId());
                for(int i = 0;i<rolename.length;i++) {
                    System.out.println("rolename；"+rolename[i]);
                    userRoleMapper.addRole(existUser.getId(),userRoleMapper.getRidByName(rolename[i]));
                }
                result.setMsg("添加成功");
                result.setSuccess(true);
                result.setDetail(user);
            }
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
