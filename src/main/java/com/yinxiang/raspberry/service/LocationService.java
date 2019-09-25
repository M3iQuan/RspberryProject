package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.bean.Area;
import com.yinxiang.raspberry.bean.Location;
import com.yinxiang.raspberry.mapper.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LocationService {

    @Autowired
    LocationMapper locationMapper;

    //1.获取单个设备GPS数据
    public Location findDataById(String device_id){
        return  locationMapper.findDataById(device_id);
    }

    public List<Area> findAllArea() {
        return locationMapper.findAllArea();
    }

    //2.获取所有设备GPS数据
    public List<Location> findAllData(){
        return  locationMapper.findAllData();
    }

    public List<Location> findDataByArea(String area_name){
        return locationMapper.findDataByArea(area_name);
    }

    public Map<String,Object> getUserAreaById(int id) {
        Map<String, Object> map = new HashMap<>();
        String areaname = locationMapper.getUserAreaByid(id);
        map.put("areaname",areaname);
        return map;
    }
    //3.新增设备GPS数据
    /*public Long saveData(Location location) {
        return locationMapper.saveData(location);
    }*/

    //4.修改设备GPS数据
    /*public Long modifyData(Location location) {
        return locationMapper.modifyData(location);
    }*/

    //5.删除设备GPS数据
    /*public Long deleteData(String device_id) {
        return locationMapper.deleteData(device_id);
    }*/
}
