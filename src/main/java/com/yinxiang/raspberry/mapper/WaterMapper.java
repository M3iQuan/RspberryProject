package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.Water;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WaterMapper {

    //1.获取单个设备的历史水浸数据数目
    Long findCountById(@Param("device_id") String device_id);

    //2.获取所有设备的历史水浸数据数目
    Long findAllCount();

    //3.获取所有设备的最新水浸数据数目
    Long findAllCountLatest();

    //4.获取单个设备的历史水浸数据并且可分页
    List<Water> findDataByIdAndPage(Map<String, Object> data);

    //5.获取单个设备的最新水浸数据
    Water findLatestDataById(@Param("device_id") String device_id);

    //6.获取所有设备的历史水浸数据并且分页
    List<Water> findAllDataByPage(Map<String, Object> data);

    //7.获取所有设备的最新水浸数据并且分页
    List<Water> findAllLatestDataByPage(Map<String, Object> data);

    //8.新增设备的水浸数据
    Long saveData(Map<String, Object> data);

    //9.修改设备的水浸数据
    Long modifyData(Water water);

    //10.删除设备的水浸数据
    Long deleteData(@Param("device_id") String device_id);
}
