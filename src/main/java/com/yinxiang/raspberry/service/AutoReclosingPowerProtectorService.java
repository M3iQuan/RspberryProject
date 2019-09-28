package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.bean.AutoReclosingPowerProtector;
import com.yinxiang.raspberry.mapper.AutoReclosingPowerProtectorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AutoReclosingPowerProtectorService {

    @Autowired
    AutoReclosingPowerProtectorMapper autoReclosingPowerProtectorMapper;

    //1.获取单个设备的历史重合闸数据数目
    public Long findCountById(String device_id) {
        return autoReclosingPowerProtectorMapper.findCountById(device_id);
    }

    //2.获取所有设备的历史重合闸数据数目
    public Long findAllCount() {
        return autoReclosingPowerProtectorMapper.findAllCount();
    }

    //3.获取所有设备的最新重合闸数据数目
    public Long findAllCountLatest() {
        return autoReclosingPowerProtectorMapper.findAllCountLatest();
    }

    //4.获取单个设备的历史重合闸数据并且可分页
    public List<AutoReclosingPowerProtector> findDataByIdAndPage(String device_id, Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return autoReclosingPowerProtectorMapper.findDataByIdAndPage(data);
    }

    //5.获取单个设备的最新重合闸数据
    public AutoReclosingPowerProtector findLatestDataById(String device_id){
        return autoReclosingPowerProtectorMapper.findLatestDataById(device_id);
    }

    //6.获取所有设备的历史重合闸数据并且分页
    public List<AutoReclosingPowerProtector> findAllDataByPage(Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return autoReclosingPowerProtectorMapper.findAllDataByPage(data);
    }

    //7.获取所有设备的最新重合闸数据并且分页
    public List<AutoReclosingPowerProtector> findAllLatestDataByPage(Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return autoReclosingPowerProtectorMapper.findAllLatestDataByPage(data);
    }

    //8.新增设备的重合闸数据
    public Long saveData(Map<String, Object> data) {
        return autoReclosingPowerProtectorMapper.saveData(data);
    }

    //9.修改设备的重合闸数据
    public Long modifyData(AutoReclosingPowerProtector autoReclosingPowerProtector) {
        return autoReclosingPowerProtectorMapper.modifyData(autoReclosingPowerProtector);
    }

    //10.删除设备的重合闸数据
    public Long deleteData(String device_id) {
        return autoReclosingPowerProtectorMapper.deleteData(device_id);
    }
}
