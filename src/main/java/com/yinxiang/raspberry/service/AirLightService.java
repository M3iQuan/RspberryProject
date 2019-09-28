package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.bean.AirLight;
import com.yinxiang.raspberry.mapper.AirLightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AirLightService {
    @Autowired
    AirLightMapper airLightMapper;

    //1.插入数据
    public Long saveData(Map<String, Object> data) {
        return airLightMapper.saveData(data);
    }

    //2.对搜索条件进行判断
    public void addCondition(Map<String, Object>data){
        if (data.get("queryString") instanceof List) {
            List<Map<String, Object>> queryString = (ArrayList)data.get("queryString");
            Iterator<Map<String,Object>> iterator = queryString.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> query = iterator.next();
                if ("luminance".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("luminance_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("luminance_lessEqual", query.get("value2"));
                    }
                } else if ("pm2_5".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("pm2_5_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("pm2_5_lessEqual", query.get("value2"));
                    }
                } else if ("pm10".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("pm10_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("pm10_lessEqual", query.get("value2"));
                    }
                }
            }
        }
        data.remove("queryString");
    }

    //3.计算单设备的历史数据进行高级搜索后的个数
    public Long findAllCount(Map<String,Object> data) {
        addCondition(data);
        return airLightMapper.findAllCount(data);
    }

    //4.计算对用户所在区域的所有设备最新数据进行高级搜索后的个数
    public Long findAllCountLatest(Map<String, Object> data) {
        addCondition(data);
        return airLightMapper.findAllCountLatest(data);
    }

    //5.对用户所在区域的所有设备的最新数据进行高级搜索
    public List<AirLight> findAllLatestDataByPage(Map<String, Object> data){
        data.put("currentPage", (((Integer)data.get("currentPage")) - 1) * (Integer)data.get("pageSize"));
        addCondition(data);
        for(String key: data.keySet()){
            System.out.println(key + " : " + data.get(key));
        }
        return airLightMapper.findAllLatestDataByPage(data);
    }

    //6.对单设备的历史数据进行高级搜索
    public List<AirLight> queryOnCondition(Map<String, Object> data){
        data.put("currentPage", (((Integer)data.get("currentPage")) - 1) * (Integer)data.get("pageSize"));
        addCondition(data);
        for(String key: data.keySet()){
            System.out.println(key + " : " + data.get(key));
        }
        return airLightMapper.queryOnCondition(data);
    }

    //7.按设备号搜索某台设备的最新数据
    public AirLight findLatestDataById(String device_id) {
        return airLightMapper.findLatestDataById(device_id);
    }



}
