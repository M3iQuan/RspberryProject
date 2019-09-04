package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.DeviceMapper;
import com.yinxiang.raspberry.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    DeviceMapper deviceMapper;

    public List<Device> getWrongDeviceById(String device_id) {
        return deviceMapper.getWrongDeviceById(device_id);
    }

    public List<Device> getDeviceByPage(Integer page, Integer size, String keywords, String id,Double latitude,Double longitude,String description,String statusname,String type,String areaname) {
        int start = (page - 1) * size;
        return deviceMapper.getDeviceByPage(start, size, keywords,id,latitude,longitude,description,statusname,type,areaname);
    }

    public Long getCountByKeywords(String keywords, String id,Double latitude,Double longitude,String description,String statusname,String type,String areaname) {
        return deviceMapper.getCountByKeywords(keywords,id,latitude,longitude,description,statusname,type,areaname);
    }

    public int updateDevice(Device device) {
        System.out.println("type:"+device.getType());
        if(device.getStatusname() != null) {
            return deviceMapper.updateDevice(device)+deviceMapper.updateDevice_status(device);
        }else {
            return deviceMapper.updateDevice(device);
        }
    }

    public List<String> getAllType() {
        return deviceMapper.getAllType();
    }

    public List<String> getAllArea() {
        return deviceMapper.getAllArea();
    }

    public int addDevice(Device device) {
        return deviceMapper.addDevice(device)+deviceMapper.addDevice_status(device.getId(),device.getStatusname());
    }


}
