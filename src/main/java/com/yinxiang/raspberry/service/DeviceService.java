package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.DeviceMapper;
import com.yinxiang.raspberry.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceService {
    @Autowired
    DeviceMapper deviceMapper;

    public List<Device> getWrongDeviceById(String device_id) {
        return deviceMapper.getWrongDeviceById(device_id);
    }

    public List<Device> getDeviceByPage(Integer page, Integer size, String keywords, String id,Double latitude,Double longitude,String description,String statusname,String type,String areaname,List<String> areanames) {
        int start = (page - 1) * size;
        return deviceMapper.getDeviceByPage(start, size, keywords,id,latitude,longitude,description,statusname,type,areaname,areanames);
    }

    public Long getCountByKeywords(String keywords, String id,Double latitude,Double longitude,String description,String statusname,String type,String areaname,List<String> areanames) {
        return deviceMapper.getCountByKeywords(keywords,id,latitude,longitude,description,statusname,type,areaname,areanames);
    }

    public int updateDevice(Device device) {
            return deviceMapper.updateDevice(device);
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

    public int deleteDevice(String device_id) {
        return deviceMapper.deleteDevice(device_id);
    }

    public Map<String,Object> batchDeleteDevice(String[] device_id){
        Map<String,Object> map = new HashMap<>();
        int count = 0;
        for (String ids:device_id) {
            count += deviceMapper.deleteDevice(ids);
            System.out.println("删除"+ids);
        }
        if(count==device_id.length) {
            map.put("msg","全部删除成功" );
            map.put("status",200 );
        }else {
            map.put("msg","没有全部删除" );
            map.put("status",400 );
        }
        return map;
    }

    public Device existDevice(String device_id) {
        return deviceMapper.existDevice(device_id);
    }
}
