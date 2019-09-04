package com.yinxiang.raspberry.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.mapper.DevicesMapper;
import com.yinxiang.raspberry.mapper.SensorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
                    Light light = lightService.findLatestDataById(device_id);
                    payload += ",\"光照强度\":{\"状态\":\""+light.getLuminance()+"lux\"}";
                }else if(sensor.getName().equals("水浸")){
                    Water water = waterService.findLatestDataById(device_id);
                    payload += ",\"水浸\":{\"状态\":\""+water.getStatus()+"\"}";
                }else if(sensor.getName().equals("pm2.5") || sensor.getName().equals("pm10")){
                    Air air = airService.findLatestDataById(device_id);
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

    //2.获取设备信息汇总
    public TotalDevices findTotalDevices(){
        TotalDevices totalDevices = new TotalDevices();
        totalDevices.setTotalNum(deviceMapper.findTotalNum());
        totalDevices.setOffLineNum(deviceMapper.findOffLineNum());
        totalDevices.setErrNum(deviceMapper.findErrNum());
        totalDevices.setTotalData(deviceMapper.findTotalData());
        return totalDevices;
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
    public List<ErrDevices> findAllErr() {
        return deviceMapper.findAllErr();
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

/*    public void getWrongDeviceById(String device_id) {
        Map<String, Object> map = new HashMap<>();
        List<com.yinxiang.raspberry.model.Device> wrongDevices = deviceService.getWrongDeviceById(device_id);
        map.put("wrongDevices",wrongDevices);
        map.put("status",200);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(map);
            String area = wrongDevices.get(0).getAreaname();
            if(area.equals("南山区")) {
                mqttService.sendToMqtt(jsonString,"user/error/ns");
            }else if(area.equals("福田区")) {
                mqttService.sendToMqtt(jsonString,"user/error/ft");
            }else if(area.equals("宝安区")) {
                mqttService.sendToMqtt(jsonString,"user/error/ba");
            }else {
                mqttService.sendToMqtt(jsonString,"user/error/lh");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }*/

}
