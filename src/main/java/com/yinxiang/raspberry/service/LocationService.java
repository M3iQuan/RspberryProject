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

    //获取所有区域名，没有根据用户信息判断
    public List<Area> findAllArea() {
        return locationMapper.findAllArea();
    }

    //根据用户信息，获取用户所在的区域
    public List<Area> getAreaByUserId(Integer id) {
        return locationMapper.getAreaByUserId(id);
    }

    //获取所有设备GPS数据
    public List<Location> findAllData(){
        return  locationMapper.findAllData();
    }

    //根据用户信息获取所在区域的所有设备GPS数据
    public List<Location> getDeviceByUserId(Integer id){
        return locationMapper.getDeviceByUserId(id);
    }

    //根据区域名获取所在区域的所有设备GPS数据
    public List<Location> findDataByArea(String area_name) {
        return locationMapper.findDataByArea(area_name);
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
