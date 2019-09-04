package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.bean.Light;
import com.yinxiang.raspberry.mapper.LightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LightService {
    @Autowired
    LightMapper lightMapper;

    //1.获取单个设备的历史光照强度数据数目
    public Long findCountById(String device_id) {
        return lightMapper.findCountById(device_id);
    }

    //2.获取所有设备的历史光照强度数据数目
    public Long findAllCount() {
        return lightMapper.findAllCount();
    }

    //3.获取所有设备的最新温光照强度数据数目
    public Long findAllCountLatest() {
        return lightMapper.findAllCountLatest();
    }

    //4.获取单个设备的历史光照强度数据并且可分页
    public List<Light> findDataByIdAndPage(String device_id, Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return lightMapper.findDataByIdAndPage(data);
    }

    //5.获取单个设备的最新光照强度数据
    public Light findLatestDataById(String device_id){
        return lightMapper.findLatestDataById(device_id);
    }

    //6.获取所有设备的历史光照强度数据并且分页
    public List<Light> findAllDataByPage(Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return lightMapper.findAllDataByPage(data);
    }

    //7.获取所有设备的最新光照强度数据并且分页
    public List<Light> findAllLatestDataByPage(Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return lightMapper.findAllLatestDataByPage(data);
    }

    //8.新增设备的光照强度数据
    public Long saveData(Light Light) {
        return lightMapper.saveData(Light);
    }

    //9.修改设备的光照强度数据
    public Long modifyData(Light Light) {
        return lightMapper.modifyData(Light);
    }

    //10.删除设备的光照强度数据
    public Long deleteData(String device_id) {
        return lightMapper.deleteData(device_id);
    }
}
