package com.yinxiang.raspberry.config;

import com.yinxiang.raspberry.bean.Test;
import com.yinxiang.raspberry.service.DeviceInformationService;
import com.yinxiang.raspberry.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
    @Scheduled(cron = "*/20 * * * * ?")
    //添加定时任务
    public void configureTasks2(){
        mqttService.sendToMqtt("device/online_test","say hello");
       /* System.out.println(" 广播状态");
        System.out.println("南山区在线设备: " + test.getNs_OnLineSet().size());
        System.out.println("宝安区在线设备: " + test.getBa_OnLineSet().size());
        System.out.println("福田区在线设备: " + test.getFt_OnLineSet().size());
        System.out.println("罗湖区在线设备: " + test.getLh_OnLineSet().size());*/
        setCount(getCount()+1);
        if(getCount() >= 2) {
            if(test.getNs_OnLineSet().size() != 0) {
                deviceInformationService.updateOnlineBySet(test.getNs_OnLineSet());
            }
            if(test.getBa_OnLineSet().size() != 0) {
                deviceInformationService.updateOnlineBySet(test.getBa_OnLineSet());
            }
            if(test.getFt_OnLineSet().size() != 0) {
                deviceInformationService.updateOnlineBySet(test.getFt_OnLineSet());
            }
            if(test.getLh_OnLineSet().size() != 0) {
                deviceInformationService.updateOnlineBySet(test.getLh_OnLineSet());
            }
            test.setNs_OnLineSet(new HashSet<>());
            test.setBa_OnLineSet(new HashSet<>());
            test.setFt_OnLineSet(new HashSet<>());
            test.setLh_OnLineSet(new HashSet<>());
            deviceInformationService.updateStates();
            //System.out.println("更新状态表");
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
