package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.bean.DeviceIP;
import com.yinxiang.raspberry.mapper.DeviceIPMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceIPService {
    @Autowired
    DeviceIPMapper deviceIPMapper;


    //1.根据设备ID查找IP地址
    public DeviceIP findIPById(Long dev_id){
        return deviceIPMapper.findIPById(dev_id);
    }

    //7.新增设备的IP地址
    public Long saveData(DeviceIP deviceIP) {
        return deviceIPMapper.saveData(deviceIP);
    }

    //8.修改设备的IP地址
    public Long modifyData(DeviceIP deviceIP) {
        return deviceIPMapper.modifyData(deviceIP);
    }

    //9.删除设备的IP地址
    public Long deleteData(Long dev_id) {
        return deviceIPMapper.deleteData(dev_id);
    }

}
