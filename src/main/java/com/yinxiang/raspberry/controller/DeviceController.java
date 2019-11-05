package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.model.UserUtils;
import com.yinxiang.raspberry.service.DeviceInformationService;
import com.yinxiang.raspberry.service.LocationService;
import com.yinxiang.raspberry.service.MqttService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "设备信息数据接口")
@RequestMapping(value = "/device")
public class DeviceController {
    @Autowired
    DeviceInformationService deviceInformationService;
    @Autowired
    MqttService mqttService;


    /**1
     * 根据用户信息获取用户所在区域设备信息汇总,即包括totalNum:设备总数 offLineNum:离线设备数 errNum:异常设备数 totalData:数据量
     * @return
     */
    @RequestMapping(value = "/totalInformation", method = RequestMethod.GET)
    public Map<String, Object> findTotalDevices(){
        return deviceInformationService.findTotalDevicesByUserId(UserUtils.getCurrentUser().getId());
    }


    /**2
     * 根据设备号获取所有传感器最新数据，用于在地图中展示数据
     * @param device_id 设备号
     * @return
     */
    @RequestMapping(value = "/sensors/map/{device_id}", method = RequestMethod.GET)
    public DeviceInformation findSensorsById(@PathVariable("device_id") String device_id){
        return deviceInformationService.findSensorsInformationById(device_id);
    }


    /**3
     * 根据设备号所有传感器最新数据，用于在设备的详细信息页面展示为卡片形式
     * @param device_id
     * @return
     */
    @RequestMapping(value = "/sensors/detail/{device_id}", method = RequestMethod.GET, produces = "application/json")
    public String findDataById(@PathVariable("device_id") String device_id){
        return deviceInformationService.findSensorsData(device_id);
    }


    /**4
     * 获取用户所在设备故障表数据
     * @return
     */
    @RequestMapping(value = "/ErrTables", method = RequestMethod.GET)
    public List<ErrDevices> findAllErr(){
        return deviceInformationService.findErrDeviceByUserId(UserUtils.getCurrentUser().getId());
    }


    /*//3.更新设备状态
    @RequestMapping(value = "/updateStates/{device_id}/{status_id}", method = RequestMethod.GET)
    public void updateOnLine(@PathVariable("device_id") String device_id,@PathVariable("status_id") Integer status_id){
        deviceInformationService.updateStates(device_id, status_id);
    }*/
}
