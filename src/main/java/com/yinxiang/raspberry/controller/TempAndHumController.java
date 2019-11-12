package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.TempAndHum;
import com.yinxiang.raspberry.model.UserUtils;
import com.yinxiang.raspberry.service.LocationService;
import com.yinxiang.raspberry.service.TempAndHumService;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "温度和湿度数据接口")
public class TempAndHumController {
    @Autowired
    TempAndHumService tempAndHumService;
    @Autowired
    LocationService locationService;


    /**1
     * 获取单个设备的历史温湿度数据数目
     * @param device_id 设备号
     * @return
     */
    @ApiOperation(value = "获取单个设备的历史温湿度数据数目", notes = "获取单个设备的历史温湿度数据数目")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/temperature_and_humidity/count/{device_id}", method = RequestMethod.GET)
    public Long findCountById(@PathVariable("device_id") String device_id) {
        return tempAndHumService.findCountById(device_id);
    }


    /**2
     * 单个设备的历史温湿度数据并且可分页，并且也可以搜索
     * @param data Map<String, Object> {"device_id":"", "pageSize":"", "currentPage":"", "queryString":[{"name":"", "value":""},{"name":"", "value1":"", "value2":""},{}]}
     * @return
     */
    @RequestMapping(value = "/device/temperature_and_humidity/query", method = RequestMethod.POST)
    public Map<String,Object> Query(@RequestBody Map<String, Object> data){
        return tempAndHumService.findDataByIdAndPage(data);
    }


    /**3
     * 获取用户所在区域的所有设备的最新温湿度数据数目
     * @return
     */
    @ApiOperation(value = "获取用户所在区域的所有设备的最新温湿度数据数目", notes = "获取用户所在区域的所有设备的最新温湿度数据数目")
    @RequestMapping(value = "/device/temperature_and_humidity/latest/count", method = RequestMethod.GET)
    public Long findAllCountLatest() {
        return tempAndHumService.findAllCountLatest(locationService.getAreaIdByUserId(UserUtils.getCurrentUser().getId()));
    }


    /**4
     * 获取用户所在区域的所有设备的最新水浸数据并且分页
     * @param data Map<String, Object> {"device_id":"", "pageSize":"", "currentPage":"", "queryString":[{"name":"", "value":""},{"name":"", "value1":"", "value2":""},{}]}
     * @return
     */
    @ApiOperation(value = "获取所有设备的最新温湿度数据并且分页", notes = "获取所有设备的最新温湿度数据")
    @RequestMapping(value = "/device/temperature_and_humidity/latest", method = RequestMethod.POST)
    public Map<String, Object> findAllLatestDataByPage(@RequestBody Map<String, Object> data) {
        return tempAndHumService.findAllLatestDataByPage(data, locationService.getAreaIdByUserId(UserUtils.getCurrentUser().getId()));
    }


    /**5
     * 获取单个设备的最新温湿度数据
     * @param device_id 设备号
     * @return
     */
    @ApiOperation(value = "获取单个设备的最新温湿度数据", notes = "获取单个设备的最新温湿度数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/temperature_and_humidity/latest/{device_id}", method = RequestMethod.GET)
    public List<TempAndHum> findLatestDataById(@PathVariable(value = "device_id") String device_id) {
        return tempAndHumService.findLatestDataById(device_id);
    }


    /**6
     * 获取所有设备的历史温湿度数据数目
     * @return
     */
    @ApiOperation(value = "获取所有设备的历史温湿度数据数目", notes = "获取所有设备的历史温湿度数据数目")
    @RequestMapping(value = "/device/temperature_and_humidity/count", method = RequestMethod.GET)
    public Long findAllCount() {
        return tempAndHumService.findAllCount();
    }

}
