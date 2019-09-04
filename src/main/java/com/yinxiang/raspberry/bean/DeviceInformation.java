package com.yinxiang.raspberry.bean;

import java.util.Map;

public class DeviceInformation {
    /**
     * 设备号
     */
    private String device_id;

    /**
     * 传感器
     */
    private Map<String,String> sensors;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Map<String, String> getSensors() {
        return sensors;
    }

    public void setSensors(Map<String, String> sensors) {
        this.sensors = sensors;
    }
}
