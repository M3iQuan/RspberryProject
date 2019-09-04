package com.yinxiang.raspberry.bean;

public class ErrDevices {
    /**
     * 设备号
     */
    private String device_id;

    /**
     * 区域
     */
    private String area_name;

    /**
     * 时间
     */
    private String create_time;

    /**
     * 备注
     */
    private String description;

    public ErrDevices() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
