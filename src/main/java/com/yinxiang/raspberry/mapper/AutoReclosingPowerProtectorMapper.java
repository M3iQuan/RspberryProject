package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.AutoReclosingPowerProtector;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AutoReclosingPowerProtectorMapper {

    //1.获取单个设备的历史重合闸数据数目
    Long findCountById(@Param("device_id") String device_id);

    //获取和高级搜索对应的数目
    Long findAllCount(Map<String, Object> data);

    //获取用户所在区域单个设备的历史数据并且高级搜索
    List<AutoReclosingPowerProtector> queryOnCondition(Map<String, Object> data);

    //获取用户所在区域的所有设备的最新数据的数目
    Long findAllCountLatest(Map<String, Object> data);

    //获取用户所在区域的所有设备的最新数据
    List<AutoReclosingPowerProtector> findAllLatestDataByPage(Map<String, Object> data);


    //4.获取单个设备的历史重合闸数据并且可分页
    List<AutoReclosingPowerProtector> findDataByIdAndPage(Map<String, Object> data);

    //5.获取单个设备的最新GPS数据
    AutoReclosingPowerProtector findLatestDataById(@Param("device_id") String device_id);

    //6.获取所有设备的历史重合闸数据并且分页
    List<AutoReclosingPowerProtector> findAllDataByPage(Map<String, Object> data);

    //7.获取所有设备的最新重合闸数据并且分页

    //8.新增设备的重合闸数据
    Long saveData(Map<String, Object> data);

    //9.修改设备的重合闸数据
    Long modifyData(AutoReclosingPowerProtector autoReclosingPowerProtector);

    //10.删除设备的重合闸数据
    Long deleteData(@Param("device_id") String device_id);
}
