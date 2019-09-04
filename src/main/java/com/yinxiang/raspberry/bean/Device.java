package com.yinxiang.raspberry.bean;

public class Device {
    /**
     * 设备号
     */
    private String device_id;

    /**
     * 类型id
     */
    private Long type_id;

    /**
     * 传感器值
     */
    private Long sensor_value;

    /**
     * 备注
     */
    private String description;

    public Device() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Long getType_id() {
        return type_id;
    }

    public void setType_id(Long type_id) {
        this.type_id = type_id;
    }

    public Long getSensor_value() {
        return sensor_value;
    }

    public void setSensor_value(Long sensor_value) {
        this.sensor_value = sensor_value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
