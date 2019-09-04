package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.AutoReclosingPowerProtector;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AutoReclosingPowerProtectorMapper {

    //1.获取单个设备的历史重合闸数据数目
    Long findCountById(@Param("device_id") String device_id);

    //2.获取所有设备的历史重合闸数据数目
    Long findAllCount();

    //3.获取所有设备的最新重合闸数据数目
    Long findAllCountLatest();

    //4.获取单个设备的历史重合闸数据并且可分页
    List<AutoReclosingPowerProtector> findDataByIdAndPage(Map<String, Object> data);

    //5.获取单个设备的最新GPS数据
    AutoReclosingPowerProtector findLatestDataById(@Param("device_id") String device_id);

    //6.获取所有设备的历史重合闸数据并且分页
    List<AutoReclosingPowerProtector> findAllDataByPage(Map<String, Object> data);

    //7.获取所有设备的最新重合闸数据并且分页
    List<AutoReclosingPowerProtector> findAllLatestDataByPage(Map<String, Object> data);

    //8.新增设备的重合闸数据
    Long saveData(AutoReclosingPowerProtector autoReclosingPowerProtector);

    //9.修改设备的重合闸数据
    Long modifyData(AutoReclosingPowerProtector autoReclosingPowerProtector);

    //10.删除设备的重合闸数据
    Long deleteData(@Param("device_id") String device_id);
}
