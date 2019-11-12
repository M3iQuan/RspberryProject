package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.mapper.LocationMapper;
import com.yinxiang.raspberry.model.UserUtils;
import com.yinxiang.raspberry.service.AirLightService;
import com.yinxiang.raspberry.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "空气光照接口")
public class AirLightController {
    @Autowired
    LocationService locationService;
    @Autowired
    AirLightService airLightService;

    /**1
     * 获取单个设备的历史空气光照数据数目
     * @param device_id 设备号
     * @return
     */
    @ApiOperation(value = "获取单个设备的历史空气光照数据数目", notes = "获取单个设备的历史空气光照数据数目")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/air_light/count/{device_id}", method = RequestMethod.GET)
    public Long findCountById(@PathVariable("device_id") String device_id) {
        return airLightService.findCountById(device_id);
    }


    /**2
     * 获取单个设备的历史空气光照数据并且可分页, 同时支持搜索
     * @param data Map<String, Object> {"device_id":"", "pageSize":"", "currentPage":"", "queryString":[{"name":"", "value":""},{"name":"", "value1":"", "value2":""},{}]}
     * @return
     */
    @ApiOperation(value = "获取单个设备的历史空气光照数据并且可分页, 同时支持搜索", notes = "获取单个设备的历史空气光照数据并且可分页, 同时支持搜索")
    @RequestMapping(value = "/device/air_light/query", method = RequestMethod.POST)
    public Map<String, Object> Query(@RequestBody Map<String, Object> data) {
        return airLightService.findDataByIdAndPage(data);
    }


    /**3
     * 获取用户所在区域的所有设备的最新空气光照数据数目
     * @return
     */
    @ApiOperation(value = "获取用户所在区域的所有设备的最新空气光照数据数目", notes = "获取用户所在区域的所有设备的最新空气光照数据数目")
    @RequestMapping(value = "/device/air_light/latest/count", method = RequestMethod.GET)
    public Long findAllCountLatest() {
        return airLightService.findAllCountLatest(locationService.getAreaIdByUserId(UserUtils.getCurrentUser().getId()));
    }


    /**4
     * 获取用户所在区域的所有设备的最新重合闸数据并且分页，同时支持搜索
     * @param data Map<String, Object> {"device_id":"", "pageSize":"", "currentPage":"", "queryString":[{"name":"", "value":""},{"name":"", "value1":"", "value2":""},{}]}
     * @return
     */
    @ApiOperation(value = "获取所有设备的最新空气光照数据并且分页", notes = "获取所有设备的最新空气光照数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true), @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)})
    @RequestMapping(value = "/device/air_light/latest", method = RequestMethod.POST)
    public Map<String, Object> findAllLatestDataByPage(@RequestBody Map<String, Object> data) {
        return airLightService.findAllLatestDataByPage(data, locationService.getAreaIdByUserId(UserUtils.getCurrentUser().getId()));
    }


    /**5
     * 获取单个设备的最新空气光照数据
     * @param device_id 设备号
     * @return
     */
    @ApiOperation(value = "获取单个设备的最新空气光照数据", notes = "获取单个设备的最新空气光照数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/air_light/latest/{device_id}", method = RequestMethod.GET)
    public List<AirLight> findLatestDataById(@PathVariable(value = "device_id") String device_id) {
        return airLightService.findLatestDataById(device_id);
    }


    /**6
     * 获取所有设备的历史空气光照数据数目
     * @return
     */
    @ApiOperation(value = "获取所有设备的历史空气光照数据数目", notes = "获取所有设备的历史空气光照数据数目")
    @RequestMapping(value = "/device/air_light/count", method = RequestMethod.GET)
    public Long findAllCount() {
        return airLightService.findAllCount();
    }

}
