package com.yinxiang.raspberry.bean;

import java.util.List;

public class DeviceSensors {

    private String device_id;

    private  Sensor sensors;

    public DeviceSensors() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Sensor getSensors() {
        return sensors;
    }

    public void setSensors(Sensor sensors) {
        this.sensors = sensors;
    }
}
