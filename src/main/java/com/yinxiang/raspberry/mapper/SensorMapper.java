package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.Sensor;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SensorMapper {

    List<Sensor> findAllSensors();

    String findDataByIdAndTable(Map<String, Object> data);
}
