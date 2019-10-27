package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.model.Menu;
import com.yinxiang.raspberry.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConfigController {
    @RequestMapping(value = "/xiaji",method = RequestMethod.GET)
    public String xiaji() {
        return "下机";
    }
}
