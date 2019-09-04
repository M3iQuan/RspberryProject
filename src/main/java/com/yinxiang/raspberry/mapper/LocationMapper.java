package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.Area;
import com.yinxiang.raspberry.bean.Location;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface LocationMapper {

    //1.获取单个设备的GPS数据
    Location findDataById(@Param("device_id") String device_id);

    List<Area> findAllArea();

    //2.获取所有设备的GPS数据
    List<Location> findAllData();

    List<Location> findDataByArea(@Param("area_name") String area_name);

    //3.新增设备的GPS数据
    //Long saveData(Location location);

    //4.修改设备的GPS闸数据
    //Long modifyData(Location location);

    //5.删除设备的GPS数据
    //Long deleteData(@Param("device_id") String device_id);
}
