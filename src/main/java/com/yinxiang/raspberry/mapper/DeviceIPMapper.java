package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.DeviceIP;
import org.apache.ibatis.annotations.Param;

public interface DeviceIPMapper {

    //1.根据设备ID查找IP地址
    DeviceIP findIPById(@Param("dev_id") Long dev_id);

    //2.新增设备的IP数据
    Long saveData(DeviceIP deviceIP);

    //3.修改设备的IP数据
    Long modifyData(DeviceIP deviceIP);

    //4.删除设备的v数据
    Long deleteData(Long dev_id);
}
