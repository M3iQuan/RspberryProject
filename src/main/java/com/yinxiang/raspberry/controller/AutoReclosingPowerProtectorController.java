package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.AutoReclosingPowerProtector;
import com.yinxiang.raspberry.model.UserUtils;
import com.yinxiang.raspberry.service.AutoReclosingPowerProtectorService;
import com.yinxiang.raspberry.service.LocationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "重合闸数据接口")
public class AutoReclosingPowerProtectorController {
    @Autowired
    AutoReclosingPowerProtectorService autoReclosingPowerProtectorService;
    @Autowired
    LocationService locationService;


    /**1
     * 获取单个设备的历史重合闸数据数目
     * @param device_id 设备号
     * @return
     */
    @ApiOperation(value = "获取单个设备的历史重合闸数据数目", notes = "获取单个设备的历史重合闸数据数目")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/auto_reclosing_power_protector/count/{device_id}", method = RequestMethod.GET)
    public Long findCountById(@PathVariable("device_id") String device_id) {
        return autoReclosingPowerProtectorService.findCountById(device_id);
    }


    /**2
     * 获取单个设备的历史重合闸数据并且可分页, 同时支持搜索
     * @param data Map<String, Object> {"device_id":"", "pageSize":"", "currentPage":"", "queryString":[{"name":"", "value":""},{"name":"", "value1":"", "value2":""},{}]}
     * @return
     */
    @ApiOperation(value = "获取单个设备的历史空气光照数据并且可分页, 同时支持搜索", notes = "获取单个设备的历史空气光照数据并且可分页, 同时支持搜索")
    @RequestMapping(value = "/device/auto_reclosing_power_protector/query", method = RequestMethod.POST)
    public Map<String, Object> Query(@RequestBody Map<String, Object> data) {
        return autoReclosingPowerProtectorService.findDataByIdAndPage(data);
    }


    /**3
     * 获取用户所在区域的所有设备的最新重合闸数据数目
     * @return
     */
    @ApiOperation(value = "获取用户所在区域的所有设备的最新重合闸数据数目", notes = "获取用户所在区域的所有设备的最新重合闸数据数目")
    @RequestMapping(value = "/device/auto_reclosing_power_protector/latest/count", method = RequestMethod.GET)
    public Long findAllCountLatest() {
        return autoReclosingPowerProtectorService.findAllCountLatest(locationService.getAreaIdByUserId(UserUtils.getCurrentUser().getId()));
    }


    /**4
     * 获取用户所在区域的所有设备的最新重合闸数据并且分页，同时支持搜索
     * @param data Map<String, Object> {"device_id":"", "pageSize":"", "currentPage":"", "queryString":[{"name":"", "value":""},{"name":"", "value1":"", "value2":""},{}]}
     * @return
     */
    @ApiOperation(value = "获取所有设备的最新重合闸数据并且分页", notes = "获取所有设备的最新重合闸数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true), @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)})
    @RequestMapping(value = "/device/auto_reclosing_power_protector/latest", method = RequestMethod.POST)
    public Map<String, Object> findAllLatestDataByPage(@RequestBody Map<String, Object> data) {
        return autoReclosingPowerProtectorService.findAllLatestDataByPage(data, locationService.getAreaIdByUserId(UserUtils.getCurrentUser().getId()));
    }


    /**5
     * 获取单个设备的最新重合闸数据
     * @param device_id 设备号
     * @return
     */
    @ApiOperation(value = "获取单个设备的最新重合闸数据", notes = "获取单个设备的最新重合闸数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/auto_reclosing_power_protector/latest/{device_id}", method = RequestMethod.GET)
    public List<AutoReclosingPowerProtector> findLatestDataById(@PathVariable(value = "device_id") String device_id) {
        return autoReclosingPowerProtectorService.findLatestDataById(device_id);
    }


    /**6
     * 获取所有设备的历史重合闸数据数目
     * @return
     */
    @ApiOperation(value = "获取所有设备的历史重合闸数据数目", notes = "获取所有设备的历史重合闸数据数目")
    @RequestMapping(value = "/device/auto_reclosing_power_protector/count", method = RequestMethod.GET)
    public Long findAllCount() {
        return autoReclosingPowerProtectorService.findAllCount();
    }

}
