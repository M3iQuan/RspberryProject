package com.yinxiang.raspberry.mapper;

import com.yinxiang.raspberry.bean.AirLight;

import java.util.List;
import java.util.Map;

public interface AirLightMapper {

    List<AirLight> search(Map<String, Object> data);

    Long saveData(Map<String, Object> data);
}
