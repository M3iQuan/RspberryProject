package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.bean.Area;
import com.yinxiang.raspberry.mapper.LocationMapper;
import com.yinxiang.raspberry.mapper.RoleMapper;
import com.yinxiang.raspberry.mapper.UserMapper;
import com.yinxiang.raspberry.mapper.UserRoleMapper;
import com.yinxiang.raspberry.model.Result;
import com.yinxiang.raspberry.model.Role;
import com.yinxiang.raspberry.model.User;
import com.yinxiang.raspberry.model.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        User existUser = userMapper.loadUserByUsername(user.getUsername());
            if(existUser == null) {
                result.setMsg("用户不存在");
            }else {
                int rid = roleMapper.getRidByUserId(existUser.getId());  //在删除之前存起来，不然找不到
                userRoleMapper.deleteRoleByUid(existUser.getId());
                if(userRoleMapper.getRidByName(rolename)==1) {  //判断是不是有人要改角色为管理员，如果有的画判断角色，只有是super可以
                    if(UserUtils.getCurrentUser().getUsername().equals("super")) {
                        userRoleMapper.addRole(existUser.getId(),userRoleMapper.getRidByName(rolename));
                    }else {
                        userRoleMapper.addRole(existUser.getId(),rid);
                    }
                }else {
                    userRoleMapper.addRole(existUser.getId(),userRoleMapper.getRidByName(rolename));
                } //这个ifelse是用来防止管理员创建管理员，好像是。如果不是超级用户来创建管理员是没有效果的


                List<Area> areas = locationMapper.getAreaByUserId(UserUtils.getCurrentUser().getId());
                List<String> Areanames = new ArrayList();
                for (Area area:areas
                ) {
                    Areanames.add(area.getArea_name());
                    System.out.println("Areanames:"+area.getArea_name());
                }
                userRoleMapper.deleteUserAreaByUid(existUser.getId(),Areanames);
                for(int i=0;i<areaname.length;i++) {
                    //还要判断这些areaname是不是在areas里
                    if(Areanames.contains(areaname[i])){
                        roleMapper.addUserArea(existUser.getId(),locationMapper.getAreaIdByAreaname(areaname[i]));
                        System.out.println("添加userarea成功");
                    }
                }
                result.setMsg("添加成功");
                result.setSuccess(true);
                result.setStatus(222);
                result.setDetail(user);
                }

        return result;
    }

}
