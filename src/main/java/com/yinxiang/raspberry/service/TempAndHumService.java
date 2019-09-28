package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.bean.TempAndHum;
import com.yinxiang.raspberry.mapper.TempAndHumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TempAndHumService {
    @Autowired
    TempAndHumMapper tempAndHumMapper;

    //1.获取单个设备的历史温湿度数据数目
    public Long findCountById(String device_id) {
        return tempAndHumMapper.findCountById(device_id);
    }

    //2.获取所有设备的历史温湿度数据数目
    public Long findAllCount() {
        return tempAndHumMapper.findAllCount();
    }

    //3.获取所有设备的最新温湿度数据数目
    public Long findAllCountLatest() {
        return tempAndHumMapper.findAllCountLatest();
    }

    //4.获取单个设备的历史温湿度数据并且可分页
    public List<TempAndHum> findDataByIdAndPage(String device_id, Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return tempAndHumMapper.findDataByIdAndPage(data);
    }

    //单个设备历史数据的高级搜索
    public void queryOnCondition(Map<String, Object> data){
        if (data.get("queryString") instanceof List) {
            List<Map<String, Object>> queryString = (LinkedList)data.get("queryString");
            Iterator<Map<String,Object>> iterator = queryString.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> query = iterator.next();
                if ("temperature".equals(query.get("name"))) {
                    if ("lessEqual".equals(query.get("opt"))) {
                        data.put("temperature_lessEqual", query.get("value"));
                    }else{
                        data.put("temperature_moreEqual", query.get("value"));
                    }
                } else if ("humidity".equals(query.get("name"))) {
                    if ("lessEqual".equals(query.get("opt"))) {
                        data.put("humidity_lessEqual", query.get("value"));
                    }else{
                        data.put("humidity_moreEqual", query.get("value"));
                    }
                } else if ("fan_state".equals(query.get("name"))) {
                    data.put("fan_state", query.get("value"));
                } else if ("fan_speed".equals(query.get("name"))) {
                    data.put("fan_speed", query.get("value"));
                } else {
                    data.put("auto_flag", query.get("value"));
                }
            }
        }
        data.remove("queryString");
        //return tempAndHumMapper.queryOnCondition(data);
        for (String key : data.keySet()) {
            System.out.println(key + " : " + data.get(key));
        }
    }

    //5.获取单个设备的最新温湿度数据
    public TempAndHum findLatestDataById(String device_id){
        return tempAndHumMapper.findLatestDataById(device_id);
    }

    //6.获取所有设备的历史温湿度数据并且分页
    public List<TempAndHum> findAllDataByPage(Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return tempAndHumMapper.findAllDataByPage(data);
    }

    //7.获取所有设备的最新温湿度数据并且分页
    public List<TempAndHum> findAllLatestDataByPage(Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return tempAndHumMapper.findAllLatestDataByPage(data);
    }

    //8.新增设备的温湿度数据
    public Long saveData(Map<String, Object> data) {
        return tempAndHumMapper.saveData(data);
    }

    //9.修改设备的温湿度数据
    public Long modifyData(TempAndHum tempAndHum) {
        return tempAndHumMapper.modifyData(tempAndHum);
    }

    //10.删除设备的温湿度数据
    public Long deleteData(String device_id) {
        return tempAndHumMapper.deleteData(device_id);
    }

    //11.更新风扇转速
    public void updateFanSpeed(String device_id, Long fan_spped){
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("fan_speed", fan_spped);
        tempAndHumMapper.updateFanSpeed(data);
    }
}
