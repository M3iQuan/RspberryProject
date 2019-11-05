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

    public List<Area> getAreaByid(int pid) {
        return locationMapper.getAreaByPid(pid);
    }

    public int addArea(String areaname,String parentname) {
        Area area = new Area();
        area.setArea_name(areaname);
        area.setParentId(locationMapper.getAreaIdByAreaname(parentname));
        locationMapper.addArea(area);
        return area.getResult();
    }

    public int deleteArea(String areaname) {
        Area area = new Area();
        area.setId(locationMapper.getAreaIdByAreaname(areaname));
        locationMapper.deleteArea(area);
        return area.getResult();
    }

    /**
     * 获取用户管理的所有区域ID
     * @param id 用户ID
     * @return
     */
    public List<String> getAreaIdByUserId(Integer id) {
        return locationMapper.getAreaIdByUserId(id);
    }

    /**
     * 获取设备所在区域的名字
     * @param device_id 设备号
     * @return
     */
    public String getAreaNameByDevice(String device_id) {
        return locationMapper.getAreaNameByDevice(device_id);
    }
}
