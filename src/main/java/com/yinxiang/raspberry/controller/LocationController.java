package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.Area;
import com.yinxiang.raspberry.bean.Location;
import com.yinxiang.raspberry.model.UserUtils;
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


    /**1
     * 根据用户信息获取所有区域
     * @return
     */
    @ApiOperation(value = "根据用户信息获取所有区域", notes = " 根据用户信息获取所有区域")
    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public List<Area> getAreaByUser() {
        return locationService.getAreaByUserId(UserUtils.getCurrentUser().getId());
    }


    /**2
     * 根据用户信息获取用户所在区域的设备信息
     * @return
     */
    @ApiOperation(value = "根据用户信息获取用户所在区域的设备信息", notes = " 根据用户信息获取用户所在区域的设备信息")
    @RequestMapping(value = "/area/location", method = RequestMethod.GET)
    public List<Location> getDeviceByUser() {
        return locationService.getDeviceByUserId(UserUtils.getCurrentUser().getId());
    }


    /**3
     * 获取某个区域的所有设备信息
     * @param area_name
     * @return
     */
    @ApiOperation(value = "获取某个区域的所有设备信息", notes = " 获取某个区域的所有设备信息")
    @RequestMapping(value = "/area/location/{area_name}", method = RequestMethod.GET)
    public List<Location> findDataByArea(@PathVariable("area_name") String area_name) {
        return locationService.findDataByArea(area_name);
    }

    @RequestMapping(value = "/area/getAllArea", method = RequestMethod.GET)
    public List<String> getAllArea() {
        return locationService.findAllArea();
    }

}
