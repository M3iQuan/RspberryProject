package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.Light;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LightMapper {
    //1.获取单个设备的历史光照强度数据数目
    Long findCountById(@Param("device_id") String device_id);

    //2.获取所有设备的历史光照强度数据数目
    Long findAllCount();

    //3.获取所有设备的最新光照强度数据数目
    Long findAllCountLatest();

    //4.获取单个设备的历史光照强度数据并且可分页
    List<Light> findDataByIdAndPage(Map<String, Object> data);

    //5.获取单个设备的最新光照强度数据
    Light findLatestDataById(@Param("device_id") String device_id);

    //6.获取所有设备的历史光照强度数据并且分页
    List<Light> findAllDataByPage(Map<String, Object> data);

    //7.获取所有设备的最新光照强度数据并且分页
    List<Light> findAllLatestDataByPage(Map<String, Object> data);

    //8.新增设备的光照强度数据
    Long saveData(Light light);

    //9.修改设备的光照强度数据
    Long modifyData(Light light);

    //10.删除设备的光照强度数据
    Long deleteData(@Param("device_id") String device_id);
}
