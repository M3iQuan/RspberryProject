package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.AutoReclosingPowerProtector;
import com.yinxiang.raspberry.service.AutoReclosingPowerProtectorService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "重合闸数据接口")
public class AutoReclosingPowerProtectorController {
    @Autowired
    AutoReclosingPowerProtectorService autoReclosingPowerProtectorService;

    //1.获取单个设备的历史重合闸数据数目
    @ApiOperation(value = "获取单个设备的历史重合闸数据数目", notes = "根据用户id查询重合闸数据数目")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/auto_reclosing_power_protector/count/{device_id}", method = RequestMethod.GET)
    public Long findCountById(@PathVariable("device_id") String device_id) {
        return autoReclosingPowerProtectorService.findCountById(device_id);
    }

    //2.获取所有设备的历史重合闸数据数目
    @ApiOperation(value = "获取所有设备的历史温湿度数据数目", notes = "获取所有设备的历史温湿度数据数目")
    @RequestMapping(value = "/device/auto_reclosing_power_protector/count", method = RequestMethod.GET)
    public Long findAllCount() {
        return autoReclosingPowerProtectorService.findAllCount();
    }

    //3.获取所有设备的最新重合闸数据数目
    @ApiOperation(value = "获取所有设备的最新重合闸数据数目", notes = "获取所有设备的最新重合闸数据数目")
    @RequestMapping(value = "/device/auto_reclosing_power_protector/latest/count", method = RequestMethod.GET)
    public Long findAllCountLatest() {
        return autoReclosingPowerProtectorService.findAllCountLatest();
    }


    //4.获取单个设备的历史重合闸数据并且可分页
    @ApiOperation(value = "获取单个设备的历史重合闸数据并且可分页", notes = "根据用户id查询重合闸数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true),
            @ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/auto_reclosing_power_protector/{device_id}/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<AutoReclosingPowerProtector> findDataByIdAndPage(@PathVariable("device_id") String device_id, @PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return autoReclosingPowerProtectorService.findDataByIdAndPage(device_id, pageSize, currentPage);
    }

    //5.获取单个设备的最新重合闸数据
    @ApiOperation(value = "获取单个设备的最新重合闸数据", notes = "获取单个设备的最新重合闸数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/auto_reclosing_power_protector/latest/{device_id}", method = RequestMethod.GET)
    public AutoReclosingPowerProtector findLatestDataById(@PathVariable(value = "device_id") String device_id) {
        return autoReclosingPowerProtectorService.findLatestDataById(device_id);
    }

    //6.获取所有设备的历史重合闸数据并且分页
    @ApiOperation(value = "获取所有设备的历史重合闸数据并且分页", notes = "获取所有设备的历史重合闸数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/auto_reclosing_power_protector/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<AutoReclosingPowerProtector> findAllDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return autoReclosingPowerProtectorService.findAllDataByPage(pageSize, currentPage);
    }

    //7.获取所有设备的最新重合闸数据并且分页
    @ApiOperation(value = "获取所有设备的最新重合闸数据并且分页", notes = "获取所有设备的最新重合闸数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/auto_reclosing_power_protector/latest/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<AutoReclosingPowerProtector> findAllLatestDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage){
        return autoReclosingPowerProtectorService.findAllLatestDataByPage(pageSize, currentPage);
    }

    //8.新增设备的重合闸数据
    @ApiOperation(value = "新增设备的重合闸数据", notes = "往数据库传入新的重合闸数据")
    @RequestMapping(value = "/device/auto_reclosing_power_protector", method = RequestMethod.POST)
    public void saveData(@RequestBody @ApiParam(name = "重合闸数据", value = "传入json格式", required = true) AutoReclosingPowerProtector autoReclosingPowerProtector) {
        autoReclosingPowerProtectorService.saveData(autoReclosingPowerProtector);
    }

    //9.修改设备的重合闸数据
    @ApiOperation(value = "修改设备的重合闸数据", notes = "修改设备的重合闸数据")
    @RequestMapping(value = "/device/auto_reclosing_power_protector", method = RequestMethod.PUT)
    public void modifyData(@RequestBody @ApiParam(name = "相应重合闸数据", value = "传入json格式", required = true) AutoReclosingPowerProtector autoReclosingPowerProtector) {
        autoReclosingPowerProtectorService.modifyData(autoReclosingPowerProtector);
    }

    //10.删除设备的重合闸数据
    @ApiOperation(value = "删除设备的重合闸数据", notes = "删除设备的重合闸数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备号", required = true)
    @RequestMapping(value = "/device/auto_reclosing_power_protector/{device_id}", method = RequestMethod.DELETE)
    public void deleteData(@PathVariable("device_id") String device_id){
        autoReclosingPowerProtectorService.deleteData(device_id); }

}
