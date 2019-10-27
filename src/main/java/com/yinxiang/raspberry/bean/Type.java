package com.yinxiang.raspberry.bean;

public class Type {

    /**
     * 种类号
     */
    private Long type_id;

    /**
     * 设备种类
     */
    private  String name;

    /**
     * 传感器数量
     */
    private Long sensor_number;

    /**
     * 传感器值
     */
    private Long sensor_value;

    /**
     * 备注
     */
    private String description;


    public Type() {
    }

    public Long getType_id() {
        return type_id;
    }

    public void setType_id(Long type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSensor_number() {
        return sensor_number;
    }

    public void setSensor_number(Long sensor_number) {
        this.sensor_number = sensor_number;
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
