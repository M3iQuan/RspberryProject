package com.yinxiang.raspberry.bean;

import java.util.Map;

public class FxxkSensor {

    private String sensor_name;

    private Map<String, String> value;

    public FxxkSensor() {

    }

    public String getSensor_name() {
        return sensor_name;
    }

    public void setSensor_name(String sensor_name) {
        this.sensor_name = sensor_name;
    }

    public Map<String, String> getValue() {
        return value;
    }

    public void setValue(Map<String, String> value) {
        this.value = value;
    }
}
