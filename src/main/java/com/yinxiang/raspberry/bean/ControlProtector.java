package com.yinxiang.raspberry.bean;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "protector", database = "intellControl")
public class ControlProtector {
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
     * 分/合闸状态
     */
    @Column(name = "Sta_H")
    private String Sta_H;

    public ControlProtector() {
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

    public String getSta_H() {
        return Sta_H;
    }

    public void setSta_H(String sta_H) {
        Sta_H = sta_H;
    }

    @Override
    public String toString() {
        return "ControlProtector{" +
                "device_id='" + device_id + '\'' +
                ", area_id='" + area_id + '\'' +
                ", Sta_H='" + Sta_H + '\'' +
                '}';
    }
}
