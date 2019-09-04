package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.Water;
import com.yinxiang.raspberry.service.WaterService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "水浸数据接口")
public class WaterController {
    @Autowired
    WaterService waterService;

    //1.获取单个设备的历史水浸数据数目
    @ApiOperation(value = "获取单个设备的历史水浸数据数目", notes = "根据用户id查询水浸数据数目")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/water/count/{device_id}", method = RequestMethod.GET)
    public Long findCountById(@PathVariable("device_id") String device_id) {
        return waterService.findCountById(device_id);
    }

    //2.获取所有设备的历史水浸数据数目
    @ApiOperation(value = "获取所有设备的历史水浸数据数目", notes = "获取所有设备的历史水浸数据数目")
    @RequestMapping(value = "/device/water/count", method = RequestMethod.GET)
    public Long findAllCount() {
        return waterService.findAllCount();
    }

    //3.获取所有设备的最新水浸数据数目
    @ApiOperation(value = "获取所有设备的最新水浸数据数目", notes = "获取所有设备的历史水浸数据数目")
    @RequestMapping(value = "/device/water/latest/count", method = RequestMethod.GET)
    public Long findAllCountLatest() {
        return waterService.findAllCountLatest();
    }

    //4.获取单个设备的历史水浸数据并且可分页
    @ApiOperation(value = "获取单个设备的历史水浸数据并且可分页", notes = "根据用户id查询水浸数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true),
            @ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/water/{device_id}/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Water> findDataByIdAndPage(@PathVariable("device_id") String device_id, @PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage", required = false) Integer currentPage) {
        return waterService.findDataByIdAndPage(device_id, pageSize, currentPage);
    }

    //5.获取单个设备的最新水浸数据
    @ApiOperation(value = "获取单个设备的最新水浸数据", notes = "获取单个设备的最新水浸数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/water/latest/{device_id}", method = RequestMethod.GET)
    public Water findLatestDataById(@PathVariable(value = "device_id") String device_id) {
        return waterService.findLatestDataById(device_id);
    }

    //6.获取所有设备的历史水浸数据并且分页
    @ApiOperation(value = "获取所有设备的历史水浸数据并且分页", notes = "获取所有设备的历史水浸数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/water/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Water> findAllDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return waterService.findAllDataByPage(pageSize, currentPage);
    }

    //7.获取所有设备的最新水浸数据并且分页
    @ApiOperation(value = "获取所有设备的最新水浸数据并且分页", notes = "获取所有设备的最新水浸数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/water/latest/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Water> findAllLatestDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return waterService.findAllLatestDataByPage(pageSize, currentPage);
    }

    //8.新增设备的水浸数据
    @ApiOperation(value = "新增设备的水浸数据", notes = "往数据库传入新的水浸数据")
    @RequestMapping(value = "/device/water", method = RequestMethod.POST)
    public void createData(@RequestBody @ApiParam(name = "水浸数据", value = "传入json格式", required = true) Water water) {
        waterService.saveData(water);
    }

    //9.修改设备的水浸数据
    @ApiOperation(value = "修改设备的水浸数据")
    @RequestMapping(value = "/device/water", method = RequestMethod.PUT)
    public void modifyData(@RequestBody @ApiParam(name = "相应水浸数据", value = "传入json格式", required = true) Water water) {
        waterService.modifyData(water);
    }

    //10.删除设备的水浸数据
    @ApiOperation(value = "删除设备的水浸数据", notes = "删除设备的水浸数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备号", required = true)
    @RequestMapping(value = "/device/water/{device_id}", method = RequestMethod.DELETE)
    public void deleteData(@PathVariable("device_id") String device_id) {
        waterService.deleteData(device_id);
    }

}
