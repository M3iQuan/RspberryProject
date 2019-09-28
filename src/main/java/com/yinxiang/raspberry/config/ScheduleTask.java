package com.yinxiang.raspberry.config;

import com.yinxiang.raspberry.bean.Test;
import com.yinxiang.raspberry.service.DeviceInformationService;
import com.yinxiang.raspberry.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@Configuration
@EnableScheduling
public class ScheduleTask {
    private int count = 0;
    @Autowired
    DeviceInformationService deviceInformationService;
    @Autowired
    MqttService mqttService;
    @Autowired
    Test test;

    //@Order(2)
    //@Async
    //指定时间间隔，每5分钟执行一次
    //@Scheduled(cron = "*/10 * * * * ?")
    //添加定时任务
    /*public void configureTasks1(){
        //重置状态表
        System.out.println("更新状态表");
        deviceInformationService.updateStates();
    }*/

    //@Order(1)
    //@Async
    //指定时间间隔，每5分钟执行一次
    @Scheduled(cron = "*/15 * * * * ?")
    //添加定时任务
    public void configureTasks2(){
        mqttService.sendToMqtt("device/online_test","say hello");
        System.out.println(getCount() + " 广播状态");
        System.out.println(test.getOnLineSet().size());
        setCount(getCount()+1);
        if(getCount() >= 2) {
            if(test.getOnLineSet().size() != 0) {
                deviceInformationService.updateOnlineBySet(test.getOnLineSet());
            }
            test.getOnLineSet().clear();
            System.out.println(test.getOnLineSet().size());
            deviceInformationService.updateStates();
            System.out.println(getCount() + "更新状态表");
            setCount(0);
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
