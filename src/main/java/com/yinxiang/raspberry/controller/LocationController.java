package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.Area;
import com.yinxiang.raspberry.bean.DeviceInformation;
import com.yinxiang.raspberry.bean.Location;
import com.yinxiang.raspberry.service.DeviceInformationService;
import com.yinxiang.raspberry.service.LocationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "设备位置数据接口")
@RequestMapping(value = "/device")
public class LocationController {

    @Autowired
    LocationService locationService;

    /*//1.获取所有设备数目
    @ApiOperation(value = "获取所有设备数目", notes = "获取所有设备数目")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long findAllCount() {
        return locationService.findAllCount();
    }*/
    
    //1.获取单个设备GPS数据
    /*@ApiOperation(value = "获取单个设备的GPS数据", notes = "根据用户id查询GPS数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/location/{device_id}", method = RequestMethod.GET)
    public Location findDataByIdAndPage(@PathVariable("device_id") String device_id) {
        return locationService.findDataById(device_id);
    }*/

    @ApiOperation(value = "获取所有区域", notes = " 获取所有区域")
    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public List<Area> findAllArea(){ return locationService.findAllArea();}

    //2.获取所有设备GPS数据
    @ApiOperation(value = "获取所有设备GPS数据", notes = " 获取所有设备GPS数据")
    @RequestMapping(value = "/area/location", method = RequestMethod.GET)
    public List<Location> findAllData() {
        return locationService.findAllData();
    }

    @ApiOperation(value = "根据区域设备GPS数据", notes = " 根据区域设备GPS数据")
    @RequestMapping(value = "/area/location/{area_name}", method = RequestMethod.GET)
    public List<Location> findDataByArea(@PathVariable("area_name") String area_name) {
        return locationService.findDataByArea(area_name);
    }
    //3.新增设备GPS数据
    /*@ApiOperation(value = "新增设备GPS数据", notes = "新增设备GPS数据")
    @RequestMapping(value = "/location", method = RequestMethod.POST)
    public void createData(@RequestBody @ApiParam(name = "设备数据", value = "传入json格式", required = true) Location location) {
        locationService.saveData(location);
    }*/

    //4.修改设备GPS数据
    /*@ApiOperation(value = "修改设备GPS数据", notes = "修改设备GPS数据")
    @RequestMapping(value = "/location", method = RequestMethod.PUT)
    public void modifyData(@RequestBody @ApiParam(name = "设备数据", value = "传入json格式", required = true) Location location) {
        locationService.modifyData(location);
    }*/

    //5.删除设备GPS数据
    /*@ApiOperation(value = "删除设备GPS数据", notes = "删除设备GPS数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备号", required = true)
    @RequestMapping(value = "/location/{device_id}", method = RequestMethod.DELETE)
    public void deleteData(@PathVariable("device_id") String device_id) {
        locationService.deleteData(device_id);
    }
*/
}
