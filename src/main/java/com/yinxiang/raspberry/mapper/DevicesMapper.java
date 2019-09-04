package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.Device;
import com.yinxiang.raspberry.bean.ErrDevices;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DevicesMapper {
    //1.获取单个设备的信息
    Device findDataById(@Param("device_id") String device_id);

    //2.获取设备总数
    Long findTotalNum();

    //3.获取离线设备总数
    Long findOffLineNum();

    //4.获取故障/异常设备总数
    Long findErrNum();

    //5.获取数据总数
    Long findTotalData();

    //2.修改单个设备的状态，参数是device_id, status_id
    void updateStateById(Map<String, Object> data);

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

    List<ErrDevices> findAllErr();
}
