package com.yinxiang.raspberry.bean;

public class AirLight {
    /**
     * 设备号
     */
    private String device_id;
    /**
     * 创建时间  YY:MM:dd HH:mm:ss
     */
    private String create_time;

    /**
     * 光照强度
     */
    private Long luminance;

    /**
     * pm2.5
     */
    private Long pm2_5;

    /**
     * pm10
     */
    private Long pm10;

    public AirLight() {
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

    public Long getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(Long pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public Long getPm10() {
        return pm10;
    }

    public void setPm10(Long pm10) {
        this.pm10 = pm10;
    }
}
