package com.yinxiang.raspberry.bean;

public class DeviceIP {
    /**
     * 设备号
     */
    private Long dev_id;

    /**
     * 设备IP
     */
    private String ip;

    public DeviceIP() {
    }

    public Long getDev_id() {
        return dev_id;
    }

    public void setDev_id(Long dev_id) {
        this.dev_id = dev_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
