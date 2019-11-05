package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.Water;
import com.yinxiang.raspberry.model.UserUtils;
import com.yinxiang.raspberry.service.LocationService;
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
    @Autowired
    LocationService locationService;


    /**1
     * 获取单个设备的历史水浸数据数目
     * @param device_id 设备号
     * @return
     */
    @ApiOperation(value = "获取单个设备的历史水浸数据数目", notes = "获取单个设备的历史水浸数据数目")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/water/count/{device_id}", method = RequestMethod.GET)
    public Long findCountById(@PathVariable("device_id") String device_id) {
        return waterService.findCountById(device_id);
    }


    /**2
     * 获取单个设备的历史水浸数据并且可分页
     * @param device_id 设备号
     * @param pageSize 页面大小
     * @param currentPage 当前页面
     * @return
     */
    @ApiOperation(value = "获取单个设备的历史水浸数据并且可分页", notes = "根据用户id查询水浸数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true),
            @ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/water/{device_id}/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Water> findDataByIdAndPage(@PathVariable("device_id") String device_id, @PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage", required = false) Integer currentPage) {
        return waterService.findDataByIdAndPage(device_id, pageSize, currentPage);
    }


    /**3
     * 获取用户所在区域的所有设备的最新水浸数据数目
     * @return
     */
    @ApiOperation(value = "获取用户所在区域的所有设备的最新水浸数据数目", notes = "获取用户所在区域的所有设备的最新水浸数据数目")
    @RequestMapping(value = "/device/water/latest/count", method = RequestMethod.GET)
    public Long findAllCountLatest() {
        return waterService.findAllCountLatest(locationService.getAreaIdByUserId(UserUtils.getCurrentUser().getId()));
    }


    /**4
     * 获取用户所在区域的所有设备的最新水浸数据并且分
     * @param pageSize 页面大小
     * @param currentPage 当前页面
     * @return
     */
    @ApiOperation(value = "获取所有设备的最新水浸数据并且分页", notes = "获取所有设备的最新水浸数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "path", name = "pageSize", value = "页面记录数目", required = true),
            @ApiImplicitParam(paramType = "path", name = "currentPage", value = "当前页面", required = true)
    })
    @RequestMapping(value = "/device/water/latest/{pageSize}/{currentPage}", method = RequestMethod.GET)
    public List<Water> findAllLatestDataByPage(@PathVariable(value = "pageSize") Integer pageSize, @PathVariable(value = "currentPage") Integer currentPage) {
        return waterService.findAllLatestDataByPage(locationService.getAreaIdByUserId(UserUtils.getCurrentUser().getId()), pageSize, currentPage);
    }


    /**5
     * 获取单个设备的最新水浸数据
     * @param device_id 设备号
     * @return
     */
    @ApiOperation(value = "获取单个设备的最新水浸数据", notes = "获取单个设备的最新水浸数据")
    @ApiImplicitParam(paramType = "path", name = "device_id", value = "设备id", required = true)
    @RequestMapping(value = "/device/water/latest/{device_id}", method = RequestMethod.GET)
    public List<Water> findLatestDataById(@PathVariable(value = "device_id") String device_id) {
        return waterService.findLatestDataById(device_id);
    }


    /**6
     * 获取所有设备的历史水浸数据数目
     * @return
     */
    @ApiOperation(value = "获取所有设备的历史水浸数据数目", notes = "获取所有设备的历史水浸数据数目")
    @RequestMapping(value = "/device/water/count", method = RequestMethod.GET)
    public Long findAllCount() {
        return waterService.findAllCount();
    }

}
