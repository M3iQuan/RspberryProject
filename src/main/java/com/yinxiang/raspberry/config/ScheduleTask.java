package com.yinxiang.raspberry.config;

import com.yinxiang.raspberry.service.DeviceInformationService;
import com.yinxiang.raspberry.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Configuration
@EnableScheduling
public class ScheduleTask {
    @Autowired
    DeviceInformationService deviceInformationService;
    @Autowired
    MqttService mqttService;

    @Async
    //指定时间间隔，每5分钟执行一次
    @Scheduled(cron = "*/7 * * * * ?")
    //添加定时任务
    public void configureTasks1(){
        //重置状态表
        /*System.out.println("更新状态表");*/
        deviceInformationService.updateStates();
        //更新异常表
        //向所有设备发送状态询问
        mqttService.sendToMqtt("device/online","土豆土豆，我是地瓜");
    }

    @Async
    //指定时间间隔，每5分钟执行一次
    @Scheduled(cron = "*/6 * * * * ?")
    //添加定时任务
    public void configureTasks2(){
        //重置状态表
        /*System.out.println("广播状态");*/
        //向所有设备发送状态询问
        mqttService.sendToMqtt("device/online","土豆土豆，我是地瓜");
    }
}
