package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.Area;
import com.yinxiang.raspberry.bean.Location;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface LocationMapper {

    //1.获取单个设备的GPS数据
    Location findDataById(@Param("device_id") String device_id);

    //获取所有区域名，没有根据用户信息判断
    List<Area> findAllArea();

    //根据用户信息，获取用户所在的区域
    List<Area> getAreaByUserId(@Param("id") Integer id );

    //根据用户信息，获取用户所在区域的所有设备信息
    List<Location> getDeviceByUserId(@Param("id") Integer id);

    //2.获取所有设备的GPS数据
    List<Location> findAllData();

    List<Location> findDataByArea(@Param("area_name") String area_name);

    List<String> getUserAreaByid(@Param("id") Integer id );

    int getAreaIdByAreaname(@Param("areaname")String areaname);

    //3.新增设备的GPS数据
    //Long saveData(Location location);

    //4.修改设备的GPS闸数据
    //Long modifyData(Location location);

    //5.删除设备的GPS数据
    //Long deleteData(@Param("device_id") String device_id);
}
