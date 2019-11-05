package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.model.UserUtils;
import com.yinxiang.raspberry.service.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "远程控制接口")
@RequestMapping(value = "/device/remoteControl")
public class RemoteControlController {
    @Autowired
    MqttService mqttService;
    @Autowired
    TempAndHumService tempAndHumService;
    @Autowired
    AutoReclosingPowerProtectorService autoReclosingPowerProtectorService;
    @Autowired
    RemoteControlService remoteControlService;
    @Autowired
    LocationService locationService;

    //message: 当前远程控制只能发送控制信息，然后等待设备更新最新数据

    /**1
     * 获取用户所在区域的所有设备的风扇的状态
     * @return
     */
    @RequestMapping(value = "/fan", method = RequestMethod.GET)
    public List<ControlFan> getAllFanStatusByUser(){
        return remoteControlService.getAllFanStatusByUser(locationService.getAreaIdByUserId(UserUtils.getCurrentUser().getId()));
    }


    /**2
     * 获取用户所在区域的所有设备的重合闸的状态
     * @return
     */
    @RequestMapping(value = "/protector", method = RequestMethod.GET)
    public List<ControlProtector> getAllProtectorStatusByUser(){
        return remoteControlService.getAllProtectorStatusByUser(locationService.getAreaIdByUserId(UserUtils.getCurrentUser().getId()));
    }


    /**3
     * 批量控制风扇
     * @param data
     */
    @RequestMapping(value = "/fan", method = RequestMethod.POST)
    public void ControlFan(@RequestBody Map<String, Object> data) {
        remoteControlService.batchControlFan(data);
    }


    /**4
     * 批量控制重合闸
     * @param data
     */
    @RequestMapping(value = "/protector", method = RequestMethod.POST)
    public void ControlProtector(@RequestBody Map<String, Object> data) {
        remoteControlService.batchControlProtector(data);
    }

    /**5
     * 调整某台设备的风速
     * @param device_id 设备号
     * @param fan_speed 风速
     */
    @RequestMapping(value = "/fanSpeed/{device_id}/{fan_speed}", method = RequestMethod.GET)
    public void fanSpeed(@PathVariable("device_id") String device_id, @PathVariable("fan_speed") int fan_speed) {
        remoteControlService.fanSpeed(device_id, ""+fan_speed);
    }


    /**6
     * 打开某台设备的风扇
     * @param device_id 设备号
     */
    @RequestMapping(value = "/fanOn/{device_id}", method = RequestMethod.GET)
    public void fanOn(@PathVariable("device_id") String device_id) {
        remoteControlService.fanOn(device_id);
    }


    /**7
     * 关闭某台设备的风扇
     * @param device_id 设备号
     */
    @RequestMapping(value = "/fanOff/{device_id}", method = RequestMethod.GET)
    public void fanOff(@PathVariable("device_id") String device_id) {
        remoteControlService.fanOff(device_id);
    }


    /**8
     * 开启某台设备的自动控制
     * @param device_id 设备号
     */
    @RequestMapping(value = "/fan/autoControlOn/{device_id}", method = RequestMethod.GET)
    public void fanAutoControlOn(@PathVariable("device_id") String device_id) {
        remoteControlService.fanAutoControlOn(device_id);
    }


    /**9
     * 关闭某台设备的自动控制
     * @param device_id 设备号
     */
    @RequestMapping(value = "/fan/autoControlOff/{device_id}", method = RequestMethod.GET)
    public void fanAutoControlOff(@PathVariable("device_id") String device_id) {
        remoteControlService.fanAutoControlOff(device_id);
    }


    /**10
     * 打开某台设备的重合闸
     * @param device_id 设备号
     */
    @RequestMapping(value = "/protectorOn/{device_id}", method = RequestMethod.GET)
    public void protectorOn(@PathVariable("device_id") String device_id) {
        remoteControlService.protectorOn(device_id);
    }


    /**11
     * 关闭某台设备的重合闸
     * @param device_id 设备号
     */
    @RequestMapping(value = "/protectorOff/{device_id}", method = RequestMethod.GET)
    public void protectorOff(@PathVariable("device_id") String device_id) {
       remoteControlService.protectorOff(device_id);
    }


    /**12
     * 重启某台设备的重合闸
     * @param device_id 设备号
     */
    @RequestMapping(value = "/protectorRestart/{device_id}", method = RequestMethod.GET)
    public void protectorRestart(@PathVariable("device_id") String device_id) {
        remoteControlService.protectorRestart(device_id);
    }
}
