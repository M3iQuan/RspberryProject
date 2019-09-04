package com.yinxiang.raspberry.controller;

import com.yinxiang.raspberry.bean.AutoReclosingPowerProtector;
import com.yinxiang.raspberry.bean.DeviceIP;
import com.yinxiang.raspberry.bean.TempAndHum;
import com.yinxiang.raspberry.mapper.DeviceIPMapper;
import com.yinxiang.raspberry.service.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/fanState/{device_id}", method = RequestMethod.GET)
    public Long fanState(@PathVariable("device_id") String device_id) {
        TempAndHum tempAndHum = tempAndHumService.findLatestDataById(device_id);
        /*String topic = "device/remoteControl/"+device_id;
        tempAndHumService.updateFanSpeed(device_id, new Long(fan_speed));
        mqttService.sendToMqtt(topic, data);
        mqttService.sendToMqtt("device/fanAutoControl/"+device_id, "" + fan_speed);*/
        return tempAndHum.getFan_state();
    }

    @RequestMapping(value = "/fanSpeed/{device_id}/{fan_speed}", method = RequestMethod.GET)
    public void fanSpeed(@PathVariable("device_id") String device_id, @PathVariable("fan_speed") int fan_speed) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"fan\",\"value\":\""+fan_speed+"\"}";
        TempAndHum tempAndHum = tempAndHumService.findLatestDataById(device_id);
        tempAndHum.setFan_state(new Long(1));
        tempAndHum.setFan_speed(new Long(fan_speed));
        System.out.println(fan_speed);
        tempAndHumService.modifyData(tempAndHum);
        tempAndHum = tempAndHumService.findLatestDataById(device_id);
        System.out.println(tempAndHum.getFan_state() + " " + tempAndHum.getFan_speed());
        mqttService.sendToMqtt(topic, data);
        mqttService.sendToMqtt("device/fanAutoControl/"+device_id, "" + fan_speed);
    }

    @RequestMapping(value = "/fanOn/{device_id}", method = RequestMethod.GET)
    public void fanOn(@PathVariable("device_id") String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"fan\",\"value\":\"200\"}";
        TempAndHum tempAndHum = tempAndHumService.findLatestDataById(device_id);
        tempAndHum.setFan_state(new Long(1));
        tempAndHum.setFan_speed(new Long(20));
        tempAndHumService.modifyData(tempAndHum);
        tempAndHum = tempAndHumService.findLatestDataById(device_id);
        System.out.println(tempAndHum.getFan_state() + " " + tempAndHum.getFan_speed());
        mqttService.sendToMqtt(topic, data);
    }

    @RequestMapping(value = "/fanOff/{device_id}", method = RequestMethod.GET)
    public void fanOff(@PathVariable("device_id") String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"fan\",\"value\":\"0\"}";
        TempAndHum tempAndHum = tempAndHumService.findLatestDataById(device_id);
        tempAndHum.setFan_state(new Long(0));
        tempAndHum.setFan_speed(new Long(0));
        System.out.println(tempAndHum.getFan_state() + " " + tempAndHum.getFan_speed());
        tempAndHumService.modifyData(tempAndHum);
        tempAndHum = tempAndHumService.findLatestDataById(device_id);
        System.out.println(tempAndHum.getFan_state() + " " + tempAndHum.getFan_speed());
        mqttService.sendToMqtt(topic, data);
    }

    @RequestMapping(value = "/fan/autoControlOn/{device_id}", method = RequestMethod.GET)
    public void fanAutoControlOn(@PathVariable("device_id") String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"fan\",\"value\":\"1000\"}";
        mqttService.sendToMqtt(topic, data);
    }
    @RequestMapping(value = "/fan/autoControlOff/{device_id}", method = RequestMethod.GET)
    public void fanAutoControlOff(@PathVariable("device_id") String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"fan\",\"value\":\"2000\"}";
        mqttService.sendToMqtt(topic, data);
    }

    @RequestMapping(value = "/protectorOn/{device_id}", method = RequestMethod.GET)
    public void protectorOn(@PathVariable("device_id") String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"protector\",\"value\":\"1\"}";
        AutoReclosingPowerProtector autoReclosingPowerProtector = autoReclosingPowerProtectorService.findLatestDataById(device_id);
        autoReclosingPowerProtector.setSta_H("合");
        autoReclosingPowerProtectorService.modifyData(autoReclosingPowerProtector);
        mqttService.sendToMqtt(topic, data);
    }

    @RequestMapping(value = "/protectorOff/{device_id}", method = RequestMethod.GET)
    public void protectorOff(@PathVariable("device_id") String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"protector\",\"value\":\"0\"}";
        AutoReclosingPowerProtector autoReclosingPowerProtector = autoReclosingPowerProtectorService.findLatestDataById(device_id);
        autoReclosingPowerProtector.setSta_H("分");
        autoReclosingPowerProtectorService.modifyData(autoReclosingPowerProtector);
        mqttService.sendToMqtt(topic, data);
    }

    @RequestMapping(value = "/protectorRestart/{device_id}", method = RequestMethod.GET)
    public void protectorRestart(@PathVariable("device_id") String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"protector\",\"value\":\"2\"}";
        AutoReclosingPowerProtector autoReclosingPowerProtector = autoReclosingPowerProtectorService.findLatestDataById(device_id);
        autoReclosingPowerProtector.setSta_H("合");
        autoReclosingPowerProtectorService.modifyData(autoReclosingPowerProtector);
        mqttService.sendToMqtt(topic, data);
    }
}
