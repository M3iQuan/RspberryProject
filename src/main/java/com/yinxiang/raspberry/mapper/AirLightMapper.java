package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.Air;
import com.yinxiang.raspberry.bean.AirLight;

import java.util.List;
import java.util.Map;

public interface AirLightMapper {

    //获取用户所在区域的所有设备的最新数据的数目
    Long findAllCountLatest(Map<String, Object> data);

    //获取用户所在区域的所有设备的最新数据
    List<AirLight> findAllLatestDataByPage(Map<String, Object> data);

    //获取用户所在区域单个设备的历史数据并且高级搜索
    List<AirLight> queryOnCondition(Map<String, Object> data);

    //获取和高级搜索对应的数目
    Long findAllCount(Map<String, Object> data);

    //获取单个设备的最新数据
    AirLight findLatestDataById(String device_id);

    //插入数据
    Long saveData(Map<String, Object> data);
}
