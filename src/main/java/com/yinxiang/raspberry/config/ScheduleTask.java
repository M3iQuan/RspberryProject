package com.yinxiang.raspberry.config;

import com.yinxiang.raspberry.Utils.InfluxDbUtils;
import com.yinxiang.raspberry.service.DeviceInformationService;
import com.yinxiang.raspberry.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableScheduling
public class ScheduleTask {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    //@Async
    //指定时间间隔，每5分钟执行一次
    @Scheduled(cron = "*/5 * * * * ?")
    //添加定时任务
    public void configureTasks(){
        simpMessagingTemplate.convertAndSend("/topic/online","online");
        //System.out.println("count of data:" + test.count_temp);
        // System.out.println("write data");
        //influxDbUtils.getInfluxDB().write(test.getTemp_data());
        /* if (test.getTemperature_and_humidity_data().size() != 0) {
            System.out.println("total data number of temperature_and_humidity: " + test.getTemperature_and_humidity_data().size());
            test.getTemperature_and_humidity_data().clear();
        }
        if (test.getAir_light_data().size() != 0) {
            System.out.println("total data number of air_light :" + test.getAir_light_data().size());
            test.getAir_light_data().clear();
        }
        if (test.getWater_data().size() != 0) {
            System.out.println("total data number of water: " + test.getWater_data().size());
            test.getWater_data().clear();
        }
        if (test.getProtector_data().size() != 0) {
            System.out.println("total data number of protector: " + test.getProtector_data().size());
            test.getProtector_data().clear();
        }*/
    }

    //@Scheduled(cron = "*/20 * * * * ?")
    //添加定时任务
    /*public void configureTasks2(){
        mqttService.sendToMqtt("device/online_test","say hello");
        System.out.println(" 广播状态");
        System.out.println("南山区在线设备: " + test.getNs_OnLineSet().size());
        System.out.println("宝安区在线设备: " + test.getBa_OnLineSet().size());
        System.out.println("福田区在线设备: " + test.getFt_OnLineSet().size());
        System.out.println("罗湖区在线设备: " + test.getLh_OnLineSet().size());
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
            System.out.println("更新状态表");
            setCount(0);
        }
    }*/


}
