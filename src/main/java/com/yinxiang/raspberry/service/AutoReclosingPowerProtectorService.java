package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.bean.AutoReclosingPowerProtector;
import com.yinxiang.raspberry.mapper.AutoReclosingPowerProtectorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AutoReclosingPowerProtectorService {

    @Autowired
    AutoReclosingPowerProtectorMapper autoReclosingPowerProtectorMapper;

    public void addCondition(Map<String, Object> data) {
        if (data.get("queryString") instanceof List) {
            List<Map<String, Object>> queryString = (ArrayList)data.get("queryString");
            Iterator<Map<String,Object>> iterator = queryString.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> query = iterator.next();
                if ("U".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("U_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("U_lessEqual", query.get("value2"));
                    }
                } else if ("I".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("I_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("I_lessEqual", query.get("value2"));
                    }
                } else if ("Iz".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("Iz_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("Iz_lessEqual", query.get("value2"));
                    }
                } else if ("Ie".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("Ie_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("Ie_lessEqual", query.get("value2"));
                    }
                } else if ("Uov".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("Uov_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("Uov_lessEqual", query.get("value2"));
                    }
                } else if ("Ulv".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("Ulv_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("Ulv_lessEqual", query.get("value2"));
                    }
                } else if ("Izset".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("Izset_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("Izset_lessEqual", query.get("value2"));
                    }
                } else if ("CurErrCnt".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("CurErrCnt_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("CurErrCnt_lessEqual", query.get("value2"));
                    }
                } else if ("LKErrCnt".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("LKErrCnt_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("LKErrCnt_lessEqual", query.get("value2"));
                    }
                } else if ("VlErrCnt".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("VlErrCnt_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("VlErrCnt_lessEqual", query.get("value2"));
                    }
                } else if ("VhErrCnt".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("VhErrCnt_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("VhErrCnt_lessEqual", query.get("value2"));
                    }
                } else if ("VOffCnt".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("VOffCnt_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("VOffCnt_lessEqual", query.get("value2"));
                    }
                } else if ("Addr".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("Addr_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("Addr_lessEqual", query.get("value2"));
                    }
                } else if ("VON_OFF".equals(query.get("name"))) {
                    if((!"".equals(query.get("value1")))){
                        data.put("VON_OFF_moreEqual", query.get("value1"));
                    }
                    if(!("".equals(query.get("value2")))){
                        data.put("VON_OFF_lessEqual", query.get("value2"));
                    }
                } else if ("Sta_H".equals(query.get("name"))){
                    if((!"".equals(query.get("value")))){
                        data.put("Sta_H", query.get("value"));
                    }
                }
            }
        }
        data.remove("queryString");
    }

    //1.获取单个设备的历史重合闸数据数目
    public Long findCountById(String device_id) {
        return autoReclosingPowerProtectorMapper.findCountById(device_id);
    }

    //2.获取所有设备的历史重合闸数据数目
    public Long findAllCount(Map<String, Object> data) {
        addCondition(data);
        return autoReclosingPowerProtectorMapper.findAllCount(data);
    }

    //3.获取所有设备的最新重合闸数据数目
    public Long findAllCountLatest(Map<String,Object> data) {
        addCondition(data);
        return autoReclosingPowerProtectorMapper.findAllCountLatest(data);
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
    public List<AutoReclosingPowerProtector> findAllLatestDataByPage(Map<String,Object> data){
        data.put("currentPage", (((Integer)data.get("currentPage")) - 1) * (Integer)data.get("pageSize"));
        addCondition(data);
        for(String key: data.keySet()){
            System.out.println(key + " : " + data.get(key));
        }
        return autoReclosingPowerProtectorMapper.findAllLatestDataByPage(data);
    }

    public List<AutoReclosingPowerProtector> queryOnCondition(Map<String, Object> data){
        data.put("currentPage", (((Integer)data.get("currentPage")) - 1) * (Integer)data.get("pageSize"));
        addCondition(data);
        for(String key: data.keySet()){
            System.out.println(key + " : " + data.get(key));
        }
        return autoReclosingPowerProtectorMapper.queryOnCondition(data);
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
