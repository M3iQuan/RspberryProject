package com.yinxiang.raspberry.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.yinxiang.raspberry.bean.Location;
import com.yinxiang.raspberry.bean.Water;
import com.yinxiang.raspberry.mapper.WaterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WaterService {
    @Autowired
    WaterMapper waterMapper;
    @Autowired
    DeviceInformationService deviceInformationService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    MqttService mqttService;
    @Autowired
    LocationService locationService;
    //1.获取单个设备的历史水浸数据数目
    public Long findCountById(String device_id) {
        return waterMapper.findCountById(device_id);
    }

    //2.获取所有设备的历史水浸数据数目
    public Long findAllCount() {
        return waterMapper.findAllCount();
    }

    //3.获取所有设备的最新温水浸数据数目
    public Long findAllCountLatest() {
        return waterMapper.findAllCountLatest();
    }

    //4.获取单个设备的历史水浸数据并且可分页
    public List<Water> findDataByIdAndPage(String device_id, Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return waterMapper.findDataByIdAndPage(data);
    }

    //5.获取单个设备的最新水浸数据
    public Water findLatestDataById(String device_id){
        return waterMapper.findLatestDataById(device_id);
    }

    //6.获取所有设备的历史水浸数据并且分页
    public List<Water> findAllDataByPage(Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return waterMapper.findAllDataByPage(data);
    }

    //7.获取所有设备的最新水浸数据并且分页
    public List<Water> findAllLatestDataByPage(Integer pageSize, Integer currentPage){
        Map<String, Object> data = new HashMap<>();
        data.put("pageSize", pageSize);
        data.put("currentPage", (currentPage - 1) * pageSize);
        return waterMapper.findAllLatestDataByPage(data);
    }

    //8.新增设备的水浸数据
    public Long saveData(Water water) {
        if("报警".equals(water.getStatus())){
            deviceInformationService.updateStates(water.getDevice_id(),new Integer(3));
            Long errNum = deviceInformationService.findErrNum(water.getDevice_id(), 3, "水浸状态异常");
            if(errNum == 0){
                deviceInformationService.insertErr(water.getDevice_id(),3,water.getCreate_time(),"水浸状态异常");
                String payload = "{\"device_id\":\""+water.getDevice_id()+"\", \"status_id\":3, \"create_time\":\""+water.getCreate_time()+"\", \"description\":\"水浸状态异常\"}";
                Location location= locationService.findDataById(water.getDevice_id());
                String area = location.getArea();
                if(area.equals("南山区")) {
                    mqttService.sendToMqtt("user/error/ns",payload);
                }else if(area.equals("福田区")) {
                    mqttService.sendToMqtt("user/error/ft",payload);
                }else if(area.equals("宝安区")) {
                    mqttService.sendToMqtt("user/error/ba",payload);
                }else {
                    mqttService.sendToMqtt("user/error/lh",payload);
                }
            }
        }else{
            Long errNum = deviceInformationService.findErrNum(water.getDevice_id(), 3, "水浸状态异常");
            if(errNum != 0){
                deviceInformationService.deleteErr(water.getDevice_id(), 3, "水浸状态异常");
            }
        }
        return waterMapper.saveData(water);
    }

    //9.修改设备的水浸数据
    public Long modifyData(Water water) {
        return waterMapper.modifyData(water);
    }

    //10.删除设备的水浸数据
    public Long deleteData(String device_id) {
        return waterMapper.deleteData(device_id);
    }


}
