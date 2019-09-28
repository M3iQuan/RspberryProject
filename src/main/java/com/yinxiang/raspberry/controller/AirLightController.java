package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.mapper.LocationMapper;
import com.yinxiang.raspberry.model.UserUtils;
import com.yinxiang.raspberry.service.AirLightService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "空气光照接口")
public class AirLightController {
    @Autowired
    LocationMapper locationMapper;
    @Autowired
    AirLightService airLightService;

    //根据用户所在的区域，获取该区域内所有设备的信息，并且实现高级搜索以及分页
    @RequestMapping(value = "/device/airLight/latest", method = RequestMethod.POST)
    public Map<String,Object> findAllLatestDataByPage(@RequestBody  Map<String, Object> data) {
        data.put("user_id", UserUtils.getCurrentUser().getId());
        Map<String, Object> result = new HashMap<>();
        result.put("result", airLightService.findAllLatestDataByPage(data));
        result.put("count", airLightService.findAllCountLatest(data));
        return result;
    }

    //根据设备号进行单设备历史数据的高级搜索，并且分页
    @RequestMapping(value = "/device/airLight/query", method = RequestMethod.POST)
    public Map<String,Object> Query(@RequestBody Map<String, Object> data){
        Map<String, Object> result = new HashMap<>();
        result.put("result", airLightService.queryOnCondition(data));
        result.put("count", airLightService.findAllCount(data));
        return result;
    }
}
