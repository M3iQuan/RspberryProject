package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.AirLightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AirLightService {
    @Autowired
    AirLightMapper airLightMapper;
    public Long saveData(Map<String, Object> data) {
        return airLightMapper.saveData(data);
    }

}
