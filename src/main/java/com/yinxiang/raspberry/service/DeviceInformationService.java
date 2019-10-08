package com.yinxiang.raspberry.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.mapper.DevicesMapper;
import com.yinxiang.raspberry.mapper.SensorMapper;
import com.yinxiang.raspberry.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeviceInformationService {
    @Autowired
    DevicesMapper deviceMapper;
    @Autowired
    SensorMapper sensorMapper;
    @Autowired
    TypeMapper typeMapper;
    @Autowired
    AutoReclosingPowerProtectorService autoReclosingPowerProtectorService;
    @Autowired
    LightService lightService;
    @Autowired
    WaterService waterService;
    @Autowired
    AirService airService;
    @Autowired
    MqttService mqttService;
    @Autowired
    TempAndHumService tempAndHumService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    AirLightService airLightService;

    //1.获取设备传感器信息
    public DeviceInformation findSensorsInformationById(String device_id) {
        Device device = deviceMapper.findDataById(device_id);
        List<Sensor> sensorList = sensorMapper.findAllSensors();
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setDevice_id(device_id);
        Map<String, String> sensors = new HashMap<>();
        for(Sensor sensor : sensorList){
            if((sensor.getValue() & device.getSensor_value()) != 0){
                Map<String, Object> data = new HashMap<>();
                data.put("device_id",device_id);
                data.put("sensor_table",sensor.getSensor_table());
                data.put("sensor_column",sensor.getSensor_column());
                sensors.put(sensor.getName(), sensorMapper.findDataByIdAndTable(data)+sensor.getUnit());
            }
        }
        deviceInformation.setSensors(sensors);
        return  deviceInformation;
    }

    public String findSensorsData(String device_id){
        Device device = deviceMapper.findDataById(device_id);
        List<Sensor> sensorList = sensorMapper.findAllSensors();
        String payload = "{";
        for(Sensor sensor : sensorList){
            if((sensor.getValue() & device.getSensor_value()) != 0){
                if(sensor.getName().equals("重合闸")){
                    AutoReclosingPowerProtector autoReclosingPowerProtector = autoReclosingPowerProtectorService.findLatestDataById(device_id);
                    payload += "\"重合闸\":{ \"状态\":\""+autoReclosingPowerProtector.getSta_H()+"\"}";
                    payload += ",\"重合闸电流\":{ \"实时电流\":\""+autoReclosingPowerProtector.getI()*0.1
                            +"A\",\"额定电流\":\""+autoReclosingPowerProtector.getIe()*0.1
                            +"A\",\"实时漏电流\":\""+autoReclosingPowerProtector.getIz()
                            +"mA\",\"漏电动作值\":\""+autoReclosingPowerProtector.getIzset()+"mA\"}";
                    payload += ",\"重合闸电压\":{ \"实时电压\":\""+autoReclosingPowerProtector.getU()
                            +"V\",\"过压动作值\":\""+autoReclosingPowerProtector.getUov()
                            +"V\",\"欠压动作值\":\""+autoReclosingPowerProtector.getUlv()
                            +"V\"}";
                    payload += ",\"重合闸电流统计数\":{ \"过流次数\":\""+autoReclosingPowerProtector.getCurErrCnt()
                            +"\",\"漏电次数\":\""+autoReclosingPowerProtector.getLKErrCnt()
                            +"\",\"断电次数\":\""+autoReclosingPowerProtector.getVOffCnt()
                            +"\"}";
                    payload += ",\"重合闸电压统计次数\":{ \"欠压次数\":\""+autoReclosingPowerProtector.getVlErrCnt()
                            +"\",\"过压次数\":\""+autoReclosingPowerProtector.getVhErrCnt() +"\"}";
                }else if(sensor.getName().equals("光照强度")){
                    AirLight light = airLightService.findLatestDataById(device_id);
                    payload += ",\"光照强度\":{\"状态\":\""+light.getLuminance()+"lux\"}";
                }else if(sensor.getName().equals("水浸")){
                    Water water = waterService.findLatestDataById(device_id);
                    payload += ",\"水浸\":{\"状态\":\""+water.getStatus()+"\"}";
                }else if(sensor.getName().equals("pm2.5") || sensor.getName().equals("pm10")){
                    AirLight air = airLightService.findLatestDataById(device_id);
                    payload += ",\"空气质量\":{\"pm2.5\":\""+air.getPm2_5()+"ug/m3\", \"pm10\":\""+air.getPm10()+"ug/m3\"}";
                }else if(sensor.getName().equals("风扇")){
                    TempAndHum tempAndHum = tempAndHumService.findLatestDataById(device_id);
                    payload += ",\"风扇\":{\"转速\":\""+tempAndHum.getFan_speed()+"\"}";
                }
            }
        }
        payload = payload + "}";
        return  payload;
    }

    //2.根据用户信息获取用户所在区域的设备信息汇总
    public TotalDevices findTotalDevicesByUserId(Integer id){
        TotalDevices totalDevices = new TotalDevices();
        totalDevices.setTotalNum(deviceMapper.findTotalNumByUserId(id));
        totalDevices.setOffLineNum(deviceMapper.findOffLineNumByUserId(id));
        totalDevices.setErrNum(deviceMapper.findErrNumByUserId(id));
        totalDevices.setTotalData(deviceMapper.findTotalData());
        return totalDevices;
    }

    public void updateOnlineBySet(HashSet<String> data){
        deviceMapper.updateOnlineBySet(data);
    }

    public void updateStatesById(String device_id, String type_id, String date_time, String device_status){
        Map<String,Object> data = new HashMap<>();
        //获取所有传感器，用于判断设备含有什么传感器
        List<Sensor> sensorList = sensorMapper.findAllSensors();
        Type deviceType = typeMapper.findDataById(new Long(type_id));
        if(device_status.charAt(0) == '1'){ //设备只有传感器故障 error
            data.put("device_id", device_id);
            data.put("status_id", new Integer(4));
            //System.out.println(device_id + "设备有传感器故障");
            for(int i = 1; i < device_status.length(); i++){
                //当设备有某个传感器时
                if((deviceType.getSensor_value() & sensorList.get(i-1).getValue()) != 0){
                    //当某个传感器故障时
                    if(device_status.charAt(i) == '1'){
                        insertErr(device_id, new Integer(4), date_time, sensorList.get(i-1).getName()+"传感器故障");
                        Map<String, Object> payload= new HashMap<>();
                        payload.put("device_id", device_id);
                        payload.put("status_id", "4");
                        payload.put("create_time", date_time);
                        payload.put("description",sensorList.get(i-1).getName()+"传感器故障");
                        mqttService.sendToMqtt("user/Order/error",payload.toString());
                    }
                }
            }
        }else if(device_status.charAt(0) == '2'){  //设备只有数据异常 warn
            data.put("device_id", device_id);
            data.put("status_id", new Integer(3));
            //System.out.println(device_id + "设备有数据异常");
            for(int i = 1; i < device_status.length(); i++){
                //当设备有某个传感器时
                if((deviceType.getSensor_value() & sensorList.get(i-1).getValue()) != 0){
                    //当某个传感器故障时
                    if(device_status.charAt(i) == '2'){
                        insertErr(device_id, new Integer(3), date_time, sensorList.get(i-1).getName()+"传感器数据异常");
                        Map<String, Object> payload= new HashMap<>();
                        payload.put("device_id", device_id);
                        payload.put("status_id", "3");
                        payload.put("create_time", date_time);
                        payload.put("description",sensorList.get(i-1).getName()+"传感器数据异常");
                        mqttService.sendToMqtt("user/Order/error",payload.toString());
                    }
                }
            }
        }else{  //设备既有传感器故障和数据异常
            data.put("device_id", device_id);
            data.put("status_id", new Integer(4));
            //System.out.println(device_id + "设备既有传感器故障又有传感器异常");
            for(int i = 1; i < device_status.length(); i++){
                //当设备有某个传感器时
                if((deviceType.getSensor_value() & sensorList.get(i-1).getValue()) != 0){
                    //当某个传感器故障时
                    if(device_status.charAt(i) == '1'){
                        insertErr(device_id, new Integer(4), date_time, sensorList.get(i-1).getName()+"传感器故障");
                        Map<String, Object> payload= new HashMap<>();
                        payload.put("device_id", device_id);
                        payload.put("status_id", "4");
                        payload.put("create_time", date_time);
                        payload.put("description",sensorList.get(i-1).getName()+"传感器故障");
                        mqttService.sendToMqtt("user/Order/error",payload.toString());
                    }
                }else if((deviceType.getSensor_value() & sensorList.get(i-1).getValue()) != 0){
                    //当某个传感器故障时
                    if(device_status.charAt(i) == '2'){
                        insertErr(device_id, new Integer(3), date_time, sensorList.get(i-1).getName()+"传感器数据异常");
                        Map<String, Object> payload= new HashMap<>();
                        payload.put("device_id", device_id);
                        payload.put("status_id", "3");
                        payload.put("create_time", date_time);
                        payload.put("description",sensorList.get(i-1).getName()+"传感器数据异常");
                        mqttService.sendToMqtt("user/Order/error", payload.toString());
                    }
                }
            }
        }
        deviceMapper.updateStateById(data);
    }


    public void updateStates(String device_id, Integer status_id){
        Map<String,Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("status_id", status_id);
        deviceMapper.updateStateById(data);
    }

    //4.更新所有设备状态
    public void updateStates(){
        //先更新状态表
        deviceMapper.updateStates();
        //再重置为离线
        deviceMapper.resetOffLine();
        //还要处理异常表
        deviceMapper.updateErrTables();
    }

    public Long insertErr(String device_id, Integer status_id, String create_time, String description){
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("status_id", status_id);
        data.put("create_time", create_time);
        data.put("description", description);
        return deviceMapper.insertErrTables(data);
    }

    //获取故障异常表的数据
    public List<ErrDevices> findErrDeviceByUserId(Integer id) {
        return deviceMapper.findErrDeviceByUserId(id);
    }

    public Long findErrNum(String device_id, Integer status_id, String description) {
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("status_id", status_id);
        data.put("description", description);
        return deviceMapper.findErr(data);
    }

    public void deleteErr(String device_id, Integer status_id, String description) {
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("status_id", status_id);
        data.put("description", description);
        deviceMapper.deleteErr(data);
    }

    //5.重置所有设备为离线状态
    public void resetOffLine(){
        deviceMapper.resetOffLine();
    }

}
