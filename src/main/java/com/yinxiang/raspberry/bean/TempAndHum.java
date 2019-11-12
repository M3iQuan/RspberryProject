package com.yinxiang.raspberry.bean;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Measurement(name = "temp", database = "intellControl")
public class TempAndHum {

    /**
     * 设备号
     */
    @Column(name = "device_id", tag = true)
    private String device_id;

    /**
     * 区域号
     */
    @Column(name = "area_id", tag = true)
    private String area_id;

    /**
     * 时间戳  YY:MM:dd HH:mm:ss
     */
    @Column(name = "time")
    private Instant time;

    /**
     * 温度 ℃
     */
    @Column(name = "temperature")
    private Double temperature;

    /**
     * 湿度 %
     */
    @Column(name = "humidity")
    private Double humidity;

    /**
     * 风扇状态 0=关闭 1=打开
     */
    @Column(name = "fan_state")
    private Long fan_state;

    /**
     * 风扇转速 0~100 0=关闭
     */
    @Column(name = "fan_speed")
    private Long fan_speed;

    /**
     * 自动控制状态 0=关闭 1=打开
     */
    @Column(name = "auto_flag")
    private Long auto_flag;

    public TempAndHum() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Long getFan_state() {
        return fan_state;
    }

    public void setFan_state(Long fan_state) {
        this.fan_state = fan_state;
    }

    public Long getFan_speed() {
        return fan_speed;
    }

    public void setFan_speed(Long fan_speed) {
        this.fan_speed = fan_speed;
    }

    public Long getAuto_flag() {
        return auto_flag;
    }

    public void setAuto_flag(Long auto_flag) {
        this.auto_flag = auto_flag;
    }

    @Override
    public String toString() {
        return "TempAndHum{" +
                "device_id='" + device_id + '\'' +
                ", area_id='" + area_id + '\'' +
                ", time=" + time +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", fan_state=" + fan_state +
                ", fan_speed=" + fan_speed +
                ", auto_flag=" + auto_flag +
                '}';
    }
}
