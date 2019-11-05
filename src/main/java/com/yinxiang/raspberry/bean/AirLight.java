package com.yinxiang.raspberry.bean;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "air_light", database = "intellControl")
public class AirLight {

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
     * 光照强度 lux
     */
    @Column(name = "luminance")
    private Long luminance;

    /**
     * pm2.5 ug/m3
     */
    @Column(name = "pm2_5")
    private Long pm2_5;

    /**
     * pm10 ug/m3
     */
    @Column(name = "pm10")
    private Long pm10;

    public AirLight() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
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

    @Override
    public String toString() {
        return "AirLight{" +
                "device_id='" + device_id + '\'' +
                ", time=" + time +
                ", luminance=" + luminance +
                ", pm2_5=" + pm2_5 +
                ", pm10=" + pm10 +
                '}';
    }
}
