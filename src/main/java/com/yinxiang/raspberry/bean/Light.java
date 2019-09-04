package com.yinxiang.raspberry.bean;

public class Light {
    /**
     * 设备号
     */
    private String device_id;
    /**
     * 创建时间  YY:MM:dd HH:mm:ss
     */
    private String create_time;

    /**
     * 光照强度 lux
     */
    private Long luminance;

    public Light() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Long getLuminance() {
        return luminance;
    }

    public void setLuminance(Long luminance) {
        this.luminance = luminance;
    }
}
