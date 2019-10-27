package com.yinxiang.raspberry.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.mapper.DeviceMapper;
import com.yinxiang.raspberry.mapper.DevicesMapper;
import com.yinxiang.raspberry.mapper.SensorMapper;
import com.yinxiang.raspberry.mapper.TypeMapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

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

    //2.获取设备传感器数据
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

    //3.根据用户信息获取用户所在区域的设备信息汇总
    public TotalDevices findTotalDevicesByUserId(Integer id){
        TotalDevices totalDevices = new TotalDevices();
        totalDevices.setTotalNum(deviceMapper.findTotalNumByUserId(id));
        totalDevices.setOffLineNum(deviceMapper.findOffLineNumByUserId(id));
        totalDevices.setErrNum(deviceMapper.findErrNumByUserId(id));
        totalDevices.setTotalData(deviceMapper.findTotalData());
        return totalDevices;
    }

    //4.批量更新数据
    public void batchUpdateStates(HashSet<String> data){
        SqlSession sqlSession = null;
        List<String> a = new ArrayList<>(data);
        try{
            sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
            int batchCount = 1000;
            int batchLastIndex = batchCount;
            for(int index = 0; index < data.size();){
                if(batchLastIndex >= data.size()){
                    batchLastIndex = data.size();
                    sqlSession.update("com.yinxiang.raspberry.mapper.DevicesMapper.updateOnlineByList", a.subList(index, batchLastIndex));
                    sqlSession.commit();
                    System.out.println("index: " + index + " batchLastIndex: " + batchLastIndex);
                    System.out.println("插入完毕");
                    break;
                }else{
                    sqlSession.update("com.yinxiang.raspberry.mapper.DevicesMapper.updateOnlineByList", a.subList(index, batchLastIndex));
                    sqlSession.commit();
                    System.out.println("index: " + index + " batchLastIndex: " + batchLastIndex);
                    index = batchLastIndex;
                    batchLastIndex = index + (batchCount - 1);
                }
            }
            sqlSession.commit();
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            sqlSession.close();
        }
    }

    public void updateOnlineBySet(HashSet<String> data){
        deviceMapper.updateOnlineBySet(data);
    }


    //修改设备状态
    @Async
    public void updateStates(String device_id, Integer status_id){
        Map<String,Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("status_id", status_id);
        deviceMapper.updateStateById(data);
    }

    /**
     * 设备上线
     * @param device_id 设备号
     */
    @Async
    public void connect(String device_id){
        deviceMapper.connect(device_id);
    }

    /**
     * 设备下线
     * @param device_id 设备号
     */
    @Async
    public void disconnect(String device_id) {
        deviceMapper.disconnect(device_id);
    }

    /**
     * 设备状态改变时进行处理，判断是从正常变成故障，还是从故障变成正常
     * @param data Map里面包含device_id(String), type_id(String), date_time(String), device_status(String)
     * @return payload Map里面包含device_id(String), status(String)
     */
    public Map<String, Object> handlerStatus(Map<String, Object> data){
        Map<String, Object> payload = new HashMap<>();
        String device_id = (String) data.get("device_id");
        String type_id = (String) data.get("type_id");
        String date_time = (String) data.get("date_time");
        String device_status = (String) data.get("device_status");
        if(device_status.equals("000000000")){ //设备恢复正常
            System.out.println(device_id + " is normal");
            updateStates(device_id, new Integer(1));
            payload.put("device_id", device_id);
            payload.put("status", "正常");
        }else { //设备有故障或异常
            System.out.println(device_id + " is err!");
            payload = judgeErrType(device_id, type_id, date_time, device_status);
        }
        return payload;
    }

    /**
     *
     * @param device_id  设备号
     * @param type_id  设备类型，可以用来获取设备拥有的传感器数量及类型
     * @param date_time  故障时间
     * @param device_status  设备状态信息，是9位的数字，类似 101000000, 第一位表示的是当前的设备状态，包含0,1,2,3,四种,0代表正常，1代表只有传感器故障，2代表数据异常，
    后面8位的各自都有3种标识，0,1,2 分别代表正常，传感器故障，数据异常
     */
    public Map<String, Object> judgeErrType(String device_id, String type_id, String date_time, String device_status){
        Map<String,Object> payload = new HashMap<>();
        payload.put("device_id", device_id);
        //获取所有传感器，用于判断设备含有什么传感器
        List<Sensor> sensorList = sensorMapper.findAllSensors();
        Type deviceType = typeMapper.findDataById(new Long(type_id));
        if(device_status.charAt(0) == '1'){ //设备只有传感器故障 error
            updateStates(device_id, new Integer(3) );
            payload.put("status", "故障");
            for(int i = 1; i < device_status.length(); i++){
                if((deviceType.getSensor_value() & sensorList.get(i-1).getValue()) != 0){ //当设备有某个传感器时
                    if(device_status.charAt(i) == '1'){ //当某个传感器故障时
                        handlerErr(device_id, new Integer(3), date_time, sensorList.get(i-1).getName()+"传感器故障");
                    }
                }
            }
        }else if(device_status.charAt(0) == '2'){  //设备只有数据异常 warn
            updateStates(device_id, new Integer(4));
            payload.put("status", "异常");
            for(int i = 1; i < device_status.length(); i++){
                if((deviceType.getSensor_value() & sensorList.get(i-1).getValue()) != 0){ //当设备有某个传感器时
                    if(device_status.charAt(i) == '2'){ //当某个传感器故障时
                        handlerErr(device_id, new Integer(4), date_time, sensorList.get(i-1).getName()+"传感器数据异常");
                    }
                }
            }
        }else{  //设备既有传感器故障和数据异常
            updateStates(device_id, new Integer(3)); //既有故障又有异常时，将状态改为故障优先
            payload.put("status", "故障");
            for(int i = 1; i < device_status.length(); i++){
                if((deviceType.getSensor_value() & sensorList.get(i-1).getValue()) != 0){ //当设备有某个传感器时
                    if(device_status.charAt(i) == '1'){ //当某个传感器故障时
                        handlerErr(device_id, new Integer(3), date_time, sensorList.get(i-1).getName()+"传感器故障");
                    } else if(device_status.charAt(i) == '2'){ //当某个传感器数据异常时
                        handlerErr(device_id, new Integer(4), date_time, sensorList.get(i-1).getName()+"传感器数据异常");
                    }
                }

            }
        }
        return payload;
    }

    /**
     * 处理设备异常的共同方法，先插入数据库，再通过mqtt发送消息创建维修单
     * @param device_id 设备号
     * @param status_id 状态id, 包含1(正常), 2(离线), 3(故障), 4(异常)
     * @param date_time 故障发生时间
     * @param description 描述信息
     */
    public void handlerErr(String device_id, Integer status_id, String date_time, String description){
        Map<String, Object> payload= new HashMap<>();
        payload.put("device_id", device_id);
        payload.put("status_id", status_id);
        payload.put("create_time", date_time);
        payload.put("description",description);
        insertErr(payload);
        payload.put("status_id", status_id.toString());
        mqttService.sendToMqtt("user/Order/error",payload.toString());
    }

    /**
     * 在数据库中插入故障设备信息
     * @param data Map中要有device_id(String), status_id(Integer类型), date_time(String), description(String)
     * @return
     */
    public Long insertErr(Map<String, Object> data) {
        return deviceMapper.insertErrTables(data);
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
