package com.yinxiang.raspberry.controller;


import com.yinxiang.raspberry.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConfigController {
    @Autowired
    MqttService mqttService;

    @RequestMapping(value = "/xiaji",method = RequestMethod.GET)
    public void xiaji() {
        mqttService.sendToMqtt("/shutdown","xiaji");

    }
}
