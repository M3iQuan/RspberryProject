package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.bean.TempAndHum;
import com.yinxiang.raspberry.mapper.TempAndHumMapper;
import io.swagger.models.auth.In;
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

    public void addCondition(Map<String,Object> data){
        if (data.get("queryString") instanceof List) {
            List<Map<String, Object>> queryString = (ArrayList)data.get("queryString");
            Iterator<Map<String,Object>> iterator = queryString.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> query = iterator.next();
                if ("temperature".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("temperature_moreEqual", new Float((String)query.get("value1")));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("temperature_lessEqual",new Float((String)query.get("value2")));
                    }
                } else if ("humidity".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("humidity_moreEqual",new Float((String)query.get("value1")));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("humidity_lessEqual", new Float((String)query.get("value2")));
                    }
                } else if ("fan_state".equals(query.get("name"))) {
                    data.put("fan_state", query.get("value"));
                } else if ("fan_speed".equals(query.get("name"))) {
                    if((!"".equals(query.get("value")))){
                        data.put("fan_speed", query.get("value"));
                    }
                } else {
                    data.put("auto_flag", query.get("value"));
                }
            }
        }
        data.remove("queryString");
    }

    //2.获取所有设备的历史温湿度数据数目
    public Long findAllCount(Map<String,Object> data) {
        addCondition(data);
        return tempAndHumMapper.findAllCount(data);
    }

    //3.获取所有设备的最新温湿度数据数目
    public Long findAllCountLatest(Map<String, Object> data) {
        addCondition(data);
        return tempAndHumMapper.findAllCountLatest(data);
    }

    //4.获取单个设备的历史温湿度数据并且可分页
    public List<TempAndHum> findDataByIdAndPage(String device_id, Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return tempAndHumMapper.findDataByIdAndPage(data);
    }

    //获取温湿度表的搜索字段
    public List<Map<String, Object>> getKeyWords(){
        List<String> keywords = tempAndHumMapper.getKeyWords();
        List<Map<String, Object>> queryString = new LinkedList<>();
        for(int i = 0; i < keywords.size(); i++){
            Map<String, Object> data = new HashMap<>();
            if(keywords.get(i).equals("device_id")){
                data.put("name", "设备号");
                data.put("type", "string");
            } else if (keywords.get(i).equals("temperature")) {
                data.put("name", "温度");
                data.put("type", "float");
            } else if (keywords.get(i).equals("humidity")) {
                data.put("name", "湿度");
                data.put("type", "float");
            } else if (keywords.get(i).equals("fan_speed")) {
                data.put("name", "风速");
                data.put("type", "float");
            } else if (keywords.get(i).equals("fan_state")) {
                data.put("name", "风扇状态");
                data.put("type", "int");
            } else if (keywords.get(i).equals("auto_flag")) {
                data.put("name", "远程控制状态");
                data.put("type", "int");
            }else {
                continue;
            }
            queryString.add(data);
        }
        return queryString;
    }

    //单个设备历史数据的高级搜索
    public List<TempAndHum> queryOnCondition(Map<String, Object> data){
        data.put("currentPage", (((Integer)data.get("currentPage")) - 1) * (Integer)data.get("pageSize"));
        addCondition(data);
        for (String key : data.keySet()) {
            System.out.println(key + " : " + data.get(key));
        }
        return tempAndHumMapper.queryOnCondition(data);
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

    //7.获取用户所在区域的所有设备的最新温湿度数据并且分页
    public List<TempAndHum> findAllLatestDataByPage(Map<String, Object> data){
        data.put("currentPage", (((Integer)data.get("currentPage")) - 1) * (Integer)data.get("pageSize"));
        addCondition(data);
        for (String key : data.keySet()) {
            System.out.println(key + " : " + data.get(key));
        }
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
