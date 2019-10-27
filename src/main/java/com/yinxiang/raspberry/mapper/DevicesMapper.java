package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.Device;
import com.yinxiang.raspberry.bean.ErrDevices;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface DevicesMapper {
    //1.获取单个设备的信息
    Device findDataById(@Param("device_id") String device_id);

    //2.根据用户信息获取用户所在区域的设备总数
    Long findTotalNumByUserId(@Param("id") Integer id);

    //3.根据用户信息获取用户所在区域的离线设备总数
    Long findOffLineNumByUserId(@Param("id") Integer id);

    //4.根据用户信息获取用户所在区域的故障/异常设备总数
    Long findErrNumByUserId(@Param("id") Integer id);

    //5.获取数据总数
    Long findTotalData();

    //2.修改单个设备的状态，参数是device_id, status_id
    void updateStateById(Map<String, Object> data);

    void updateOnlineBySet(HashSet<String> data);

    void updateOnlineByList(ArrayList<String> data);

    //3.更新所有设备状态
    void updateStates();

    //4.重置所有设备为离线状态
    void resetOffLine();

    void updateErrTables();

    //插入故障
    Long insertErrTables(Map<String, Object> data);

    //查找故障
    Long findErr(Map<String, Object> data);

    //删除故障
    void deleteErr(Map<String, Object> data);

    //根据用户信息获取所在区域的所有异常/故障信息
    List<ErrDevices> findErrDeviceByUserId(@Param("id") Integer id);

    //设备上线
    void connect(@Param("device_id") String device_id);
    //设备下线
    void disconnect(@Param("device_id") String device_id);
}
