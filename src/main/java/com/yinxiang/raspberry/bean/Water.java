package com.yinxiang.raspberry.bean;

public class Water {
    /**
     * 设备号
     */
    private String device_id;
    /**
     * 创建时间  YY:MM:dd HH:mm:ss
     */
    private String create_time;

    /**
     * 水浸状态：正常/报警
     */
    private String status;

    public Water() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
