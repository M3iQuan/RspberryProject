package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.model.*;
import com.yinxiang.raspberry.service.*;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController

@RequestMapping("/basic")
public class Controller {
    @Autowired
    private UserService userService;

    @Autowired
    private WebService webService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private RoleService roleService;




    @PostMapping("/regis")
    public Result register(User user){
        return userService.register(user);
    }

    @PostMapping(value = "/changePassword")
    public Result changePassword(User user,String newPassword) {
        return userService.changePassword(user,newPassword);
    }

    @PostMapping(value = "/resetPassword")
    public Result resetPassword(User user) {
        return userService.changePassword(user,user.getUsername());
    }

    @PostMapping(value = "/deleteUser")
    public Result deleteUser(String username) {
        return userService.deleteUser(username);
    }

    @RequestMapping(value = "/addRole", method = RequestMethod.POST)  //一个一个添加角色
    public Result addRole(User user,String rolename,String[] areaname) {
        return roleService.addRole(user,rolename,areaname);
    }


    @RequestMapping(value = "/changeDetail",method = RequestMethod.POST)  //修改用户信息
    public Result changeDetail(User user) {
        return userService.changeDetail(user);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Map<String, Object> getUserByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String keywords,
            String username, String description,String rolename
            ) {
        Map<String, Object> map = new HashMap<>();
        List<User> userByPage = webService.getUserByPage(page, size,
                keywords,username,description,rolename);
        List<Role> allRole = roleService.getAllRole();
//        for(User value:userByPage){
//            System.out.println(value.getUsername());
//        }

        Long count = webService.getCountByKeywords(keywords,username,description,rolename);
        map.put("users", userByPage);
        map.put("roles",allRole);
        map.put("count", count);
        return map;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result updateUser(User user) {
        Result result = new Result();
        System.out.println(user.toString());
        if (webService.updateUser(user) == 1) {
            System.out.println("更新成功!");
            result.setMsg("更新成功!");
            result.setSuccess(true);
            result.setStatus(200);
            return result;
        }
        System.out.println("更新失败!");
        result.setMsg("更新失败!");
        result.setSuccess(false);
        result.setStatus(400);
        return result;
    }


    @RequestMapping(value = "/getAllMenu", method = RequestMethod.GET)
    public Map<String, Object> getAllMenu() {
        Map<String, Object> map = new HashMap<>();
        List<Menu> menus = menuService.getAllMenu();
        map.put("menus",menus);
        map.put("status", 200);
        return map;
    }


    @RequestMapping(value = "/Permission", method = RequestMethod.POST)
    public Result permission(String username,String[] menunames) {
        return menuService.permission(username,menunames);
    }

    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    public Map<String, Object> getDeviceByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String keywords,
            String id,Double latitude,Double longitude,String description,String statusname,String type,String areaname
    ) {
        Map<String, Object> map = new HashMap<>();
        List<Device> deviceByPage = deviceService.getDeviceByPage(page, size,
                keywords,id,latitude,longitude,description,statusname,type,areaname);
        List<String> types = deviceService.getAllType();
        List<String> areas = deviceService.getAllArea();
        Long count = deviceService.getCountByKeywords(keywords,id,latitude,longitude,description,statusname,type,areaname);
        map.put("types",types);
        map.put("areas",areas);
        map.put("devices", deviceByPage);
        map.put("count", count);
        return map;
    }

    @RequestMapping(value = "/updatedevice", method = RequestMethod.POST)
    public Result updateDevice(Device device) {
        Result result = new Result();
        if (deviceService.updateDevice(device) != 0) {
            result.setMsg("更新成功!");
            result.setSuccess(true);
            result.setStatus(200);
            return result;
        }
        result.setMsg("更新失败!");
        result.setSuccess(false);
        result.setStatus(400);
        return result;
    }

    @RequestMapping(value = "/addDevice",method = RequestMethod.POST)
    public Result addDevice(Device device) {
        System.out.println("device:"+device.toString());
        Result result = new Result();
        if(deviceService.addDevice(device) == 2) {
            result.setMsg("添加成功!");
            result.setSuccess(true);
            result.setStatus(200);
            return result;
        }else {
            result.setMsg("添加失败!");
            result.setSuccess(false);
            result.setStatus(400);
            return result;
        }
    }

    @RequestMapping(value = "AuthorityAllocation",method = RequestMethod.POST) //权限分配
    public Result changeRole(User user,String rolename,String[] areaname) {
        return roleService.addRole(user,rolename,areaname);
    }


}
