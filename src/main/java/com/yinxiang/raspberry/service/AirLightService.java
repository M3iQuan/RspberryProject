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

    public Long saveData(Map<String, Object> data) {
        return airLightMapper.saveData(data);
    }

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

    public Long findAllCount(Map<String,Object> data) {
        addCondition(data);
        return airLightMapper.findAllCount(data);
    }

    public Long findAllCountLatest(Map<String, Object> data) {
        addCondition(data);
        return airLightMapper.findAllCountLatest(data);
    }

    public AirLight findLatestDataById(String device_id) {
        return airLightMapper.findLatestDataById(device_id);
    }

    public List<AirLight> findAllLatestDataByPage(Map<String, Object> data){
        data.put("currentPage", (((Integer)data.get("currentPage")) - 1) * (Integer)data.get("pageSize"));
        addCondition(data);
        for(String key: data.keySet()){
            System.out.println(key + " : " + data.get(key));
        }
        return airLightMapper.findAllLatestDataByPage(data);
    }

    public List<AirLight> queryOnCondition(Map<String, Object> data){
        data.put("currentPage", (((Integer)data.get("currentPage")) - 1) * (Integer)data.get("pageSize"));
        addCondition(data);
        for(String key: data.keySet()){
            System.out.println(key + " : " + data.get(key));
        }
        return airLightMapper.queryOnCondition(data);
    }

}
