package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.model.Menu;
import com.yinxiang.raspberry.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConfigController {
    @Autowired
    MenuService menuService;

//    @RequestMapping("/config/sysmenu")
//    public List<Menu> sysmenu() {
//        return menuService.getMenusByHrId();
//    }
}
