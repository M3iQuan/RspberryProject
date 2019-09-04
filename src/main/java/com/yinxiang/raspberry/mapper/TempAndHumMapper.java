package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.TempAndHum;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TempAndHumMapper {

    //1.获取单个设备的历史温湿度数据数目
    Long findCountById(@Param("device_id") String device_id);

    //2.获取所有设备的历史温湿度数据数目
    Long findAllCount();

    //3.获取所有设备的最新温湿度数据数目
    Long findAllCountLatest();

    //4.获取单个设备的历史温湿度数据并且可分页
    List<TempAndHum> findDataByIdAndPage(Map<String, Object> data);

    //5.获取单个设备的最新温湿度数据
    TempAndHum findLatestDataById(@Param("device_id") String device_id);

    //6.获取所有设备的历史温湿度数据并且分页
    List<TempAndHum> findAllDataByPage(Map<String, Object> data);

    //7.获取所有设备的最新温湿度数据并且分页
    List<TempAndHum> findAllLatestDataByPage(Map<String, Object> data);

    //8.新增设备的温湿度数据
    Long saveData(TempAndHum tempAndHum);

    //9.修改设备的温湿度数据
    Long modifyData(TempAndHum tempAndHum);

    //10.删除设备的温湿度数据
    Long deleteData(@Param("device_id") String device_id);

    //11.更新风扇转速
    void updateFanSpeed(Map<String, Object> data);
}
