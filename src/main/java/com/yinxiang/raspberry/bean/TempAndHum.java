package com.yinxiang.raspberry.bean;

public class TempAndHum {

    /**
     * 设备号
     */
    private String device_id;
    /**
     * 创建时间  YY:MM:dd HH:mm:ss
     */
    private String create_time;
    /**
     * 温度 ℃
     */
    private String temperature;
    /**
     * 湿度 %
     */
    private String humidity;

    /**
     * 风扇状态
     */
    private Long fan_state;
    /**
     * 风扇转速
     */
    private Long fan_speed;

    private Long auto_flag;

    public TempAndHum() {
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Long getFan_speed() {
        return fan_speed;
    }

    public void setFan_speed(Long fan_speed) {
        this.fan_speed = fan_speed;
    }

    public Long getFan_state() {
        return fan_state;
    }

    public void setFan_state(Long fan_state) {
        this.fan_state = fan_state;
    }

    public Long getAuto_flag() {
        return auto_flag;
    }

    public void setAuto_flag(Long auto_flag) {
        this.auto_flag = auto_flag;
    }
}
