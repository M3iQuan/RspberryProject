package com.yinxiang.raspberry.service;


import com.yinxiang.raspberry.bean.Area;
import com.yinxiang.raspberry.mapper.LocationMapper;
import com.yinxiang.raspberry.mapper.RoleMapper;
import com.yinxiang.raspberry.mapper.UserMapper;
import com.yinxiang.raspberry.mapper.UserRoleMapper;
import com.yinxiang.raspberry.model.Result;
import com.yinxiang.raspberry.model.User;
import com.yinxiang.raspberry.model.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    LocationMapper locationMapper;

    @Autowired
    RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("账户不存在!");
        }

        user.setRoles(userMapper.getUserRolesByUid(user.getId()));
        return user;
    }

    public Result register(User user,String rolename,String[] areaname) { //注册的服务
        for(int i=0;i<areaname.length;i++) {
            System.out.println("添加用户的areaname:"+areaname[i]);
        }
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            User existUser = userMapper.loadUserByUsername(user.getUsername());
            if(existUser != null){
                result.setMsg("用户名已存在");
            }else{
                String username = user.getUsername();
                System.out.println(user);
                String password = new BCryptPasswordEncoder().encode(username);  //
                user.setPassword(password);//这三行是密码加密
                System.out.println("getpassword:"+user.getPassword());
                if(userMapper.register(user)!=0) {   //这是把用户信息添加到user表，但是角色和区域没有划分。
                    System.out.println("添加到user成功！");
                }


                //这下面的跟roleservice的addRole方法很像
                if(userRoleMapper.getRidByName(rolename)==1) {
                    if(UserUtils.getCurrentUser().getUsername().equals("super")) {
                        userRoleMapper.addRole(userMapper.loadUserByUsername(user.getUsername()).getId(),userRoleMapper.getRidByName(rolename));
                    }else {
                        userMapper.deleteUserByName(user.getUsername());
                    }
                }else {
                    userRoleMapper.addRole(userMapper.loadUserByUsername(user.getUsername()).getId(),userRoleMapper.getRidByName(rolename));
                }

                List<Area> areas = locationMapper.getAreaByUserId(UserUtils.getCurrentUser().getId());
                List<String> Areanames = new ArrayList();
                for (Area area:areas
                ) {
                    Areanames.add(area.getArea_name());
                }
                for(int i=0;i<areaname.length;i++) {
                    if(Areanames.contains(areaname[i])){
                        System.out.println(userMapper.loadUserByUsername(user.getUsername()).getId()+"   "+locationMapper.getAreaIdByAreaname(areaname[i]));
                        roleMapper.addUserArea(userMapper.loadUserByUsername(user.getUsername()).getId(),locationMapper.getAreaIdByAreaname(areaname[i]));
                    }
                }

                result.setMsg("注册成功");
                result.setStatus(200);
                result.setSuccess(true);
                //result.setDetail(user);
            }
        } catch (Exception e) {
            userMapper.deleteUserByName(user.getUsername());
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public Result changeDetail(User user) {    //修改信息的
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            userMapper.deleteUserByName(user.getUsername());
            if(userMapper.register(user)==0) {
                result.setMsg("修改失败!");
            }else {
                result.setMsg("修改成功");
                result.setSuccess(true);
                result.setDetail(user);
            }
        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public Result deleteUser(String username) {
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        if(userMapper.deleteUserByName(username) == 0) {
            result.setMsg("删除失败！");
        }else {
            result.setMsg("删除成功！");
            result.setSuccess(true);
            result.setStatus(200);
        }
        return result;
    }

    public Result changePassword(User user,String newPassword) { //改密码的
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            Long userId= userMapper.login(user);
            if(userId == null){
                result.setMsg("用户名或密码错误");
            }else{
                userMapper.ChangePassword(user.getUsername(),newPassword);
                result.setMsg("修改成功");
                result.setSuccess(true);
                result.setDetail(user);
            }

        }catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 返回管理这台设备的所有管理人员，返回的是用户名
     * @param device_id 设备号
     * @return
     */
    public List<String> findAllUserByDeviceId(String device_id){
        return userMapper.findAllUserByDeviceId(device_id);
    }
}
