package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.bean.Air;
import com.yinxiang.raspberry.mapper.AirMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AirService {
    @Autowired
    AirMapper airMapper;

    //1.获取单个设备的历史空气质量数据数目
    public Long findCountById(String device_id) {
        return airMapper.findCountById(device_id);
    }

    //2.获取所有设备的历史空气质量数据数目
    public Long findAllCount() {
        return airMapper.findAllCount();
    }

    //3.获取所有设备的最新温空气质量数据数目
    public Long findAllCountLatest() {
        return airMapper.findAllCountLatest();
    }

    //4.获取单个设备的历史空气质量数据并且可分页
    public List<Air> findDataByIdAndPage(String device_id, Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return airMapper.findDataByIdAndPage(data);
    }

    //5.获取单个设备的最新空气质量数据
    public Air findLatestDataById(String device_id){
        return airMapper.findLatestDataById(device_id);
    }

    //6.获取所有设备的历史空气质量数据并且分页
    public List<Air> findAllDataByPage(Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return airMapper.findAllDataByPage(data);
    }

    //7.获取所有设备的最新空气质量数据并且分页
    public List<Air> findAllLatestDataByPage(Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return airMapper.findAllLatestDataByPage(data);
    }

    //8.新增设备的空气质量数据
    public Long saveData(Air Air) {
        return airMapper.saveData(Air);
    }

    //9.修改设备的空气质量数据
    public Long modifyData(Air Air) {
        return airMapper.modifyData(Air);
    }

    //10.删除设备的空气质量数据
    public Long deleteData(String device_id) {
        return airMapper.deleteData(device_id);
    }
}
