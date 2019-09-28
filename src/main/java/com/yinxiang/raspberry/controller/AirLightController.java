package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.mapper.LocationMapper;
import com.yinxiang.raspberry.model.UserUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "测试接口")
public class AirLightController {
    @Autowired
    LocationMapper locationMapper;

    @RequestMapping(value = "/device/airlight/test",  method = RequestMethod.GET)
    public List<Location> test(@RequestParam(value = "name", required = false, defaultValue = "test") String name){
        return  locationMapper.getDeviceByUserId(UserUtils.getCurrentUser().getId());
    }

    @RequestMapping(value = "/device/{area}/edit", method = RequestMethod.GET)
    public String area(@PathVariable(value = "area") String area){
        return area;
    }

    @RequestMapping(value = "/device/json", method = RequestMethod.POST)
    public String receiveJson(@RequestBody Map<String, Object> data){
        for(String key: data.keySet()){
            System.out.println(key + data.get(key).getClass().getName());
        }
        return "200";
    }
}
