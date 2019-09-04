package com.yinxiang.raspberry.mapper;


import com.yinxiang.raspberry.bean.Air;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AirMapper {
    //1.获取单个设备的历史空气质量数据数目
    Long findCountById(@Param("device_id") String device_id);

    //2.获取所有设备的历史空气质量数据数目
    Long findAllCount();

    //3.获取所有设备的最新空气质量数据数目
    Long findAllCountLatest();

    //4.获取单个设备的历史空气质量数据并且可分页
    List<Air> findDataByIdAndPage(Map<String, Object> data);

    //5.获取单个设备的最新空气质量数据
    Air findLatestDataById(@Param("device_id") String device_id);

    //6.获取所有设备的历史空气质量数据并且分页
    List<Air> findAllDataByPage(Map<String, Object> data);

    //7.获取所有设备的最新空气质量数据并且分页
    List<Air> findAllLatestDataByPage(Map<String, Object> data);

    //8.新增设备的空气质量数据
    Long saveData(Air air);

    //9.修改设备的空气质量数据
    Long modifyData(Air air);

    //10.删除设备的空气质量数据
    Long deleteData(@Param("device_id") String device_id);
}
