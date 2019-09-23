package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.LocationMapper;
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

    @Autowired
    LocationMapper locationMapper;

    public List<Role> getAllRole() {
        return roleMapper.getAllRole();
    }

    public Result addRole(User user, String rolename,String[] areaname) {   //修改角色的,角色只能是一种，但是区域可能有多个。
        Result result = new Result();                                       //这个方法不仅添加角色还添加用户所属区域。
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
                userRoleMapper.addRole(existUser.getId(),userRoleMapper.getRidByName(rolename));
                for(int i=0;i<areaname.length;i++) {
                    roleMapper.addUserArea(existUser.getId(),locationMapper.getAreaIdByAreaname(areaname[i]));
                }
                //这里还差添加用户地区的表。

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
