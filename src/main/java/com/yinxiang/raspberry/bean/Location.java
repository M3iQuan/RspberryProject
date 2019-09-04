package com.yinxiang.raspberry.bean;

public class Location {

    /**
     * 设备号
     */
    private String device_id;

    /**
     * 纬度 N
     */
    private String latitude;

    /**
     * 经度 E
     */
    private String longitude;

    /**
     * 区域
     */
    private String area;

    /**
     * 状态
     */
    private String status;

    public Location() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
