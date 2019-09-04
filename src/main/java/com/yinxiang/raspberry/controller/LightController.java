package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.Light;
import com.yinxiang.raspberry.service.LightService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "光照强度数据接口")
public class LightController {
    @Autowired
    LightService lightService;

    //1.获取单个设备的历史光照强度数据数目
    @ApiOperation(value = "获取单个设备的历史光照强度数据数目", notes = "根据用户id查询光照强度数据数目")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/light/count/{device_id}", method = RequestMethod.GET)
    public Long findCountById(@PathVariable("device_id") String device_id) {
        return lightService.findCountById(device_id);
    }

    //2.获取所有设备的历史光照强度数据数目
    @ApiOperation(value = "获取所有设备的历史光照强度数据数目", notes = "获取所有设备的历史光照强度数据数目")
    @RequestMapping(value = "/device/light/count", method = RequestMethod.GET)
    public Long findAllCount() {
        return lightService.findAllCount();
    }

    //3.获取所有设备的最新光照强度数据数目
    @ApiOperation(value = "获取所有设备的最新光照强度数据数目", notes = "获取所有设备的历史光照强度数据数目")
    @RequestMapping(value = "/device/light/latest/count", method = RequestMethod.GET)
    public Long findAllCountLatest() {
        return lightService.findAllCountLatest();
    }

    //4.获取单个设备的历史光照强度数据并且可分页
    @ApiOperation(value = "获取单个设备的历史光照强度数据并且可分页", notes = "根据用户id查询光照强度数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true),
            @ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/light/{device_id}/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Light> findDataByIdAndPage(@PathVariable("device_id") String device_id, @PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage", required = false) Integer currentPage) {
        return lightService.findDataByIdAndPage(device_id, pageSize, currentPage);
    }

    //5.获取单个设备的最新光照强度数据
    @ApiOperation(value = "获取单个设备的最新光照强度数据", notes = "获取单个设备的最新光照强度数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/light/latest/{device_id}", method = RequestMethod.GET)
    public Light findLatestDataById(@PathVariable(value = "device_id") String device_id) {
        return lightService.findLatestDataById(device_id);
    }

    //6.获取所有设备的历史光照强度数据并且分页
    @ApiOperation(value = "获取所有设备的历史光照强度数据并且分页", notes = "获取所有设备的历史光照强度数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/light/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Light> findAllDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return lightService.findAllDataByPage(pageSize, currentPage);
    }

    //7.获取所有设备的最新光照强度数据并且分页
    @ApiOperation(value = "获取所有设备的最新光照强度数据并且分页", notes = "获取所有设备的最新光照强度数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/light/latest/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Light> findAllLatestDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return lightService.findAllLatestDataByPage(pageSize, currentPage);
    }

    //8.新增设备的光照强度数据
    @ApiOperation(value = "新增设备的光照强度数据", notes = "往数据库传入新的光照强度数据")
    @RequestMapping(value = "/device/light", method = RequestMethod.POST)
    public void createData(@RequestBody @ApiParam(name = "光照强度数据", value = "传入json格式", required = true) Light light) {
        lightService.saveData(light);
    }

    //9.修改设备的光照强度数据
    @ApiOperation(value = "修改设备的光照强度数据")
    @RequestMapping(value = "/device/light", method = RequestMethod.PUT)
    public void modifyData(@RequestBody @ApiParam(name = "相应光照强度数据", value = "传入json格式", required = true) Light light) {
        lightService.modifyData(light);
    }

    //10.删除设备的光照强度数据
    @ApiOperation(value = "删除设备的光照强度数据", notes = "删除设备的光照强度数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备号", required = true)
    @RequestMapping(value = "/device/light/{device_id}", method = RequestMethod.DELETE)
    public void deleteData(@PathVariable("device_id") String device_id) {
        lightService.deleteData(device_id);
    }

}
