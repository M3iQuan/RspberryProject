package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.service.DeviceInformationService;
import com.yinxiang.raspberry.service.LocationService;
import com.yinxiang.raspberry.service.MqttService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "设备信息数据接口")
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    DeviceInformationService deviceInformationService;
    @Autowired
    MqttService mqttService;

    //1.获取设备信息汇总
    @RequestMapping(value = "/totalInformation", method = RequestMethod.GET)
    public TotalDevices findTotalDevices(){
        return deviceInformationService.findTotalDevices();
    }

    //2.获取单个设备传感器信息
    @RequestMapping(value = "/sensors/{device_id}", method = RequestMethod.GET)
    public DeviceInformation findSensorsById(@PathVariable("device_id") String device_id){
        return deviceInformationService.findSensorsInformationById(device_id);
    }

    //2.获取单个设备传感器信息
    @RequestMapping(value = "/test/{device_id}", method = RequestMethod.GET, produces = "application/json")
    public String findDataById(@PathVariable("device_id") String device_id){
        return deviceInformationService.findSensorsData(device_id);
    }

    //3.更新设备状态
    @RequestMapping(value = "/updateStates/{device_id}/{status_id}", method = RequestMethod.GET)
    public void updateOnLine(@PathVariable("device_id") String device_id,@PathVariable("status_id") Integer status_id){
        deviceInformationService.updateStates(device_id, status_id);
    }

    //4.获取设备故障表数据
    @RequestMapping(value = "/ErrTables", method = RequestMethod.GET)
    public List<ErrDevices> findAllErr(){
        return deviceInformationService.findAllErr();
    }

    @RequestMapping(value = "/mqtt/{topic}/{data}", method = RequestMethod.GET)
    public void sendToMqtt(@PathVariable("topic") String topic, @PathVariable("data") String data) {
        mqttService.sendToMqtt(topic, data);
    }

}
