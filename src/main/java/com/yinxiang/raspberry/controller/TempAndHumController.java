package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.TempAndHum;
import com.yinxiang.raspberry.service.TempAndHumService;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "温度和湿度数据接口")
public class TempAndHumController {
    @Autowired
    TempAndHumService tempAndHumService;


    //1.获取单个设备的历史温湿度数据数目
    @ApiOperation(value = "获取单个设备的历史温湿度数据数目", notes = "根据用户id查询温湿度数据数目")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/temperature_and_humidity/count/{device_id}", method = RequestMethod.GET)
    public Long findCountById(@PathVariable("device_id") String device_id) {
        return tempAndHumService.findCountById(device_id);
    }

    //2.获取所有设备的历史温湿度数据数目
    @ApiOperation(value = "获取所有设备的历史温湿度数据数目", notes = "获取所有设备的历史温湿度数据数目")
    @RequestMapping(value = "/device/temperature_and_humidity/count", method = RequestMethod.GET)
    public Long findAllCount() {
        return tempAndHumService.findAllCount();
    }

    //3.获取所有设备的最新温湿度数据数目
    @ApiOperation(value = "获取所有设备的最新温湿度数据数目", notes = "获取所有设备的历史温湿度数据数目")
    @RequestMapping(value = "/device/temperature_and_humidity/latest/count", method = RequestMethod.GET)
    public Long findAllCountLatest() {
        return tempAndHumService.findAllCountLatest();
    }

    //4.获取单个设备的历史温湿度数据并且可分页
    @ApiOperation(value = "获取单个设备的历史温湿度数据并且可分页", notes = "根据用户id查询温湿度数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true),
            @ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/temperature_and_humidity/{device_id}/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<TempAndHum> findDataByIdAndPage(@PathVariable("device_id") String device_id, @PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage", required = false) Integer currentPage) {
        return tempAndHumService.findDataByIdAndPage(device_id, pageSize, currentPage);
    }

    //单个设备历史数据的高级搜索
    /*@RequestMapping(value = "/device/temperature_and_humidity/query", method = RequestMethod.POST)
    public List<TempAndHum> Query(@RequestBody Map<String, Object> data){
        return tempAndHumService.queryOnCondition(data);
    }*/

    //5.获取单个设备的最新温湿度数据
    @ApiOperation(value = "获取单个设备的最新温湿度数据", notes = "获取单个设备的最新温湿度数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/temperature_and_humidity/latest/{device_id}", method = RequestMethod.GET)
    public TempAndHum findLatestDataById(@PathVariable(value = "device_id") String device_id) {
        return tempAndHumService.findLatestDataById(device_id);
    }

    //6.获取所有设备的历史温湿度数据并且分页
    @ApiOperation(value = "获取所有设备的历史温湿度数据并且分页", notes = "获取所有设备的历史温湿度数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/temperature_and_humidity/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<TempAndHum> findAllDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return tempAndHumService.findAllDataByPage(pageSize, currentPage);
    }

    //7.获取所有设备的最新温湿度数据并且分页
    @ApiOperation(value = "获取所有设备的最新温湿度数据并且分页", notes = "获取所有设备的最新温湿度数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/temperature_and_humidity/latest/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<TempAndHum> findAllLatestDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return tempAndHumService.findAllLatestDataByPage(pageSize, currentPage);
    }

    //8.新增设备的温湿度数据
    /*@ApiOperation(value = "新增设备的温湿度数据", notes = "往数据库传入新的温湿度数据")
    @RequestMapping(value = "/device/temperature_and_humidity", method = RequestMethod.POST)
    public void createData(@RequestBody @ApiParam(name = "温湿度数据", value = "传入json格式", required = true) TempAndHum tempAndHum) {
        tempAndHumService.saveData(tempAndHum);
    }*/

    //9.修改设备的温湿度数据
    @ApiOperation(value = "修改设备的温湿度数据", notes = "在进行了风扇转速调整了,修改相应的数据")
    @RequestMapping(value = "/device/temperature_and_humidity", method = RequestMethod.PUT)
    public void modifyData(@RequestBody @ApiParam(name = "相应温湿度数据", value = "传入json格式", required = true) TempAndHum tempAndHum) {
        tempAndHumService.modifyData(tempAndHum);
    }

    //10.删除设备的温湿度数据
    @ApiOperation(value = "删除设备的温湿度数据", notes = "删除设备的温湿度数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备号", required = true)
    @RequestMapping(value = "/device/temperature_and_humidity/{device_id}", method = RequestMethod.DELETE)
    public void deleteData(@PathVariable("device_id") String device_id) {
        tempAndHumService.deleteData(device_id);
    }

}
