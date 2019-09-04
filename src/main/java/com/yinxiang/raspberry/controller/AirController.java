package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.Air;
import com.yinxiang.raspberry.service.AirService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "空气质量数据接口")
public class AirController {
    @Autowired
    AirService airService;

    //1.获取单个设备的历史空气质量数据数目
    @ApiOperation(value = "获取单个设备的历史空气质量数据数目", notes = "根据用户id查询空气质量数据数目")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/air/count/{device_id}", method = RequestMethod.GET)
    public Long findCountById(@PathVariable("device_id") String device_id) {
        return airService.findCountById(device_id);
    }

    //2.获取所有设备的历史空气质量数据数目
    @ApiOperation(value = "获取所有设备的历史空气质量数据数目", notes = "获取所有设备的历史空气质量数据数目")
    @RequestMapping(value = "/device/air/count", method = RequestMethod.GET)
    public Long findAllCount() {
        return airService.findAllCount();
    }

    //3.获取所有设备的最新空气质量数据数目
    @ApiOperation(value = "获取所有设备的最新空气质量数据数目", notes = "获取所有设备的历史空气质量数据数目")
    @RequestMapping(value = "/device/air/latest/count", method = RequestMethod.GET)
    public Long findAllCountLatest() {
        return airService.findAllCountLatest();
    }

    //4.获取单个设备的历史空气质量数据并且可分页
    @ApiOperation(value = "获取单个设备的历史空气质量数据并且可分页", notes = "根据用户id查询空气质量数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true),
            @ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/air/{device_id}/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Air> findDataByIdAndPage(@PathVariable("device_id") String device_id, @PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage", required = false) Integer currentPage) {
        return airService.findDataByIdAndPage(device_id, pageSize, currentPage);
    }

    //5.获取单个设备的最新空气质量数据
    @ApiOperation(value = "获取单个设备的最新空气质量数据", notes = "获取单个设备的最新空气质量数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/air/latest/{device_id}", method = RequestMethod.GET)
    public Air findLatestDataById(@PathVariable(value = "device_id") String device_id) {
        return airService.findLatestDataById(device_id);
    }

    //6.获取所有设备的历史空气质量数据并且分页
    @ApiOperation(value = "获取所有设备的历史空气质量数据并且分页", notes = "获取所有设备的历史空气质量数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/air/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Air> findAllDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return airService.findAllDataByPage(pageSize, currentPage);
    }

    //7.获取所有设备的最新空气质量数据并且分页
    @ApiOperation(value = "获取所有设备的最新空气质量数据并且分页", notes = "获取所有设备的最新空气质量数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/air/latest/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Air> findAllLatestDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return airService.findAllLatestDataByPage(pageSize, currentPage);
    }

    //8.新增设备的空气质量数据
    @ApiOperation(value = "新增设备的空气质量数据", notes = "往数据库传入新的空气质量数据")
    @RequestMapping(value = "/device/air", method = RequestMethod.POST)
    public void createData(@RequestBody @ApiParam(name = "空气质量数据", value = "传入json格式", required = true) Air air) {
        airService.saveData(air);
    }

    //9.修改设备的空气质量数据
    @ApiOperation(value = "修改设备的空气质量数据")
    @RequestMapping(value = "/device/air", method = RequestMethod.PUT)
    public void modifyData(@RequestBody @ApiParam(name = "相应空气质量数据", value = "传入json格式", required = true) Air air) {
        airService.modifyData(air);
    }

    //10.删除设备的空气质量数据
    @ApiOperation(value = "删除设备的空气质量数据", notes = "删除设备的空气质量数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备号", required = true)
    @RequestMapping(value = "/device/air/{device_id}", method = RequestMethod.DELETE)
    public void deleteData(@PathVariable("device_id") String device_id) {
        airService.deleteData(device_id);
    }

}
