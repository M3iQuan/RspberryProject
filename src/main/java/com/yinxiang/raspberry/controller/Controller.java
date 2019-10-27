package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.Area;
import com.yinxiang.raspberry.model.*;
import com.yinxiang.raspberry.service.*;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private LocationService locationService;


    @PostMapping("/regis")
    public Result register(User user, String rolename, String[] areaname) {
        return userService.register(user, rolename, areaname);
    }

    @PostMapping(value = "/changePassword")
    public Result changePassword(User user, String newPassword) {
        return userService.changePassword(user, newPassword);
    }

    @PostMapping(value = "/resetPassword")
    public Result resetPassword(User user) {
        return userService.changePassword(user, user.getUsername());
    }

    @PostMapping(value = "/deleteUser")
    public Result deleteUser(String username) {
        return userService.deleteUser(username);
    }

    @RequestMapping(value = "/addRole", method = RequestMethod.POST)  //一个一个添加角色
    public Result addRole(User user, String rolename, String[] areaname) {
        return roleService.addRole(user, rolename, areaname);
    }


    @RequestMapping(value = "/changeDetail", method = RequestMethod.POST)  //修改用户信息
    public Result changeDetail(User user) {
        return userService.changeDetail(user);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Map<String, Object> getUserByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String keywords,
            String username, String description, String rolename
    ) {
        int issuper = 0;
        if("super".equals(UserUtils.getCurrentUser().getUsername())) {
            issuper = 1;
        }
        List<Area> areas = locationService.getAreaByUserId(UserUtils.getCurrentUser().getId());
        List<String> areaname = new ArrayList();
        for (Area area : areas
        ) {
            areaname.add(area.getArea_name());
        }
        Map<String, Object> map = new HashMap<>();
        List<User> userByPage = webService.getUserByPage(page, size,
                keywords, username, description, rolename, areaname,issuper);

        List<Role> allRole = roleService.getAllRole();


        for (User value : userByPage) { //这里是把展示的用户所属的区域存到areaname2，然后存到user发上去。
            List<String> areaname2 = new ArrayList();
            List<Area> areas2 = locationService.getAreaByUserId(value.getId());
            for (Area area2 : areas2
            ) {
                areaname2.add(area2.getArea_name());
            }
            value.setAreaname(areaname2);
        }

        Long count = webService.getCountByKeywords(keywords, username, description, rolename, areaname,issuper);
        map.put("users", userByPage);
        map.put("roles", allRole);   //前端用来权限分配的时候展示的
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
        map.put("menus", menus);
        map.put("status", 200);
        return map;
    }


    @RequestMapping(value = "/Permission", method = RequestMethod.POST)
    public Result permission(String username, String[] menunames) {
        return menuService.permission(username, menunames);
    }

    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    public Map<String, Object> getDeviceByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String keywords,
            String id, Double latitude, Double longitude, String description, String statusname, String type, String areaname
    ) {
        List<Area> allarea = locationService.getAreaByUserId(UserUtils.getCurrentUser().getId());
        List<String> areanames = new ArrayList();
        for (Area area : allarea
        ) {
            areanames.add(area.getArea_name());
        }
        Map<String, Object> map = new HashMap<>();
        List<Device> deviceByPage = deviceService.getDeviceByPage(page, size,
                keywords, id, latitude, longitude, description, statusname, type, areaname, areanames);
        List<String> types = deviceService.getAllType();
        List<String> areas = deviceService.getAllArea();
        Long count = deviceService.getCountByKeywords(keywords, id, latitude, longitude, description, statusname, type, areaname, areanames);
        map.put("types", types);
        map.put("areas", areas);
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

    @RequestMapping(value = "/addDevice", method = RequestMethod.POST)
    public Result addDevice(Device device) {
        System.out.println("device:" + device.toString());
        Result result = new Result();
        if (deviceService.addDevice(device) == 2) {
            result.setMsg("添加成功!");
            result.setSuccess(true);
            result.setStatus(200);
            return result;
        } else {
            result.setMsg("添加失败!");
            result.setSuccess(false);
            result.setStatus(400);
            return result;
        }
    }
    @RequestMapping(value = "/deleteDevice", method = RequestMethod.POST)
    public Result deleteDevice(@RequestBody Map<String,Object> data) {
        System.out.println("device_id:" + data.get("device_id"));
        Result result = new Result();
        if (deviceService.deleteDevice((String)data.get("device_id")) == 1) {
            result.setMsg("删除成功!");
            result.setSuccess(true);
            result.setStatus(200);
            return result;
        } else {
            result.setMsg("删除失败!");
            result.setSuccess(false);
            result.setStatus(400);
            return result;
        }
    }

    @RequestMapping(value = "/AuthorityAllocation", method = RequestMethod.POST) //权限分配
    public Result changeRole(User user, String rolename, String[] areaname) {
        for (int i = 0; i < areaname.length; i++) {
            System.out.println("AuthorityAllocation的areaname" + areaname[i]);
        }
        return roleService.addRole(user, rolename, areaname);
    }

    @RequestMapping(value = "/getAreaTree",method = RequestMethod.POST)
    public List<Area> getAreaByid(int pid) {
        return locationService.getAreaByid(pid);
    }

    @RequestMapping(value = "/addArea",method = RequestMethod.POST)
    public Map<String,Object> addArea(String areaname,String parentname) {
        Map<String, Object> map = new HashMap<>();
        if(locationService.addArea(areaname,parentname)==1) {
            map.put("status","success" );
            return map;
        }
        map.put("status","fail" );
        return map;
    }

    @RequestMapping(value = "/deleteArea", method = RequestMethod.POST)
    public Map<String,Object> deleteDep(String areaname) {
        Map<String, Object> map = new HashMap<>();
        if (locationService.deleteArea(areaname) == 1) {
            map.put("status", "success");
            map.put("msg","删除成功！" );
            return map;
        }
        map.put("status", "fail");
        map.put("msg","删除失败！" );
        return map;
    }

}