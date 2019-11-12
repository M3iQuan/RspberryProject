package com.yinxiang.raspberry.bean;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "water_status", database = "intellControl")
public class Water {
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
     * 水浸状态：正常/报警
     */
    @Column(name = "status")
    private String status;

    public Water() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Water{" +
                "device_id='" + device_id + '\'' +
                ", area_id='" + area_id + '\'' +
                ", time=" + time +
                ", status='" + status + '\'' +
                '}';
    }
}
