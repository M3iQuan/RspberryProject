package com.yinxiang.raspberry.bean;

public class Air {
    /**
     * 设备号
     */
    private String device_id;
    /**
     * 创建时间  YY:MM:dd HH:mm:ss
     */
    private String create_time;

    /**
     * pm2.5 ug/m3
     */
    private Integer pm2_5;

    /**
     * pm10 ug/m3
     */
    private Integer pm10;

    public Air() {
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

    public Integer getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(Integer pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public Integer getPm10() {
        return pm10;
    }

    public void setPm10(Integer pm10) {
        this.pm10 = pm10;
    }
}
