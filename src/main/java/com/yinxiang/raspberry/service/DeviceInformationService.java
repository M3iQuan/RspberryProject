package com.yinxiang.raspberry.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yinxiang.raspberry.Utils.InfluxDbUtils;
import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.mapper.*;
import com.yinxiang.raspberry.model.UserUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    UserMapper userMapper;
    @Autowired
    AutoReclosingPowerProtectorService autoReclosingPowerProtectorService;
    @Autowired
    WaterService waterService;
    @Autowired
    MqttService mqttService;
    @Autowired
    TempAndHumService tempAndHumService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    AirLightService airLightService;
    @Autowired
    LocationService locationService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    InfluxDbUtils influxDbUtils;

    /**1
     * 通过表名，列名和设备号查找某个数据
     * @param device_id 设备号
     * @param table 表名
     * @param column 列名
     * @return
     */
    public String findSensorDataByIdAndTableAndColumn(String device_id, String table, String column) {
        String result = "null"; //如果数据库中没有该设备的数据，则返回null
        Query query = new Query("select " + column + " from " + table + " where device_id = '" + device_id + "' order by time desc limit 1");
        QueryResult queryResult = influxDbUtils.getInfluxDB().query(query);
        try {
            result = queryResult.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString();
        }catch (Exception e){}
        finally {
            return result;
        }
    }


    /**2
     * 根据设备号获取设备传感器信息,用于地图中显示，与下面的区别在于重合闸只显示了Sta_H
     * @param device_id 设备号
     * @return
     */
    public DeviceInformation findSensorsInformationById(String device_id) {
        Device device = deviceMapper.findDataById(device_id);
        List<Sensor> sensorList = sensorMapper.findAllSensors();  //获取数据库中当前的所有传感器类型
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setDevice_id(device_id);
        Map<String, String> sensors = new HashMap<>();
        for(Sensor sensor : sensorList){
            if((sensor.getValue() & device.getSensor_value()) != 0) //判断设备是否包含某个传感器
                sensors.put(sensor.getName(), findSensorDataByIdAndTableAndColumn(device_id, sensor.getSensor_table(), sensor.getSensor_column())+sensor.getUnit());
        }
        deviceInformation.setSensors(sensors);
        return  deviceInformation;
    }


    /**3
     * 根据设备号获取设备传感器信息，该方法不会返回温湿度数据
     * @param device_id 设备号
     * @return
     */
    public String findSensorsData(String device_id){
        Device device = deviceMapper.findDataById(device_id);
        List<Sensor> sensorList = sensorMapper.findAllSensors();
        String payload = "{";
        for(Sensor sensor : sensorList){
            if((sensor.getValue() & device.getSensor_value()) != 0){ //判断设备是否包含某个传感器
                if(sensor.getName().equals("重合闸")){ //设备包含重合闸
                    AutoReclosingPowerProtector autoReclosingPowerProtector = autoReclosingPowerProtectorService.findLatestDataById(device_id).get(0);
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
                }else if(sensor.getName().equals("光照强度")){  //设备包含光照
                    AirLight light = airLightService.findLatestDataById(device_id).get(0);
                    payload += ",\"光照强度\":{\"状态\":\""+light.getLuminance()+"lux\"}";
                }else if(sensor.getName().equals("水浸")){ //设备包含水浸
                    Water water = waterService.findLatestDataById(device_id).get(0);
                    payload += ",\"水浸\":{\"状态\":\""+water.getStatus()+"\"}";
                }else if(sensor.getName().equals("pm2.5") || sensor.getName().equals("pm10")){ //设备包含空气质量
                    AirLight air = airLightService.findLatestDataById(device_id).get(0);
                    payload += ",\"空气质量\":{\"pm2.5\":\""+air.getPm2_5()+"ug/m3\", \"pm10\":\""+air.getPm10()+"ug/m3\"}";
                }else if(sensor.getName().equals("风扇")){ //设备包含风扇
                    TempAndHum tempAndHum = tempAndHumService.findLatestDataById(device_id).get(0);
                    payload += ",\"风扇\":{\"转速\":\""+tempAndHum.getFan_speed()+"\"}";
                }
            }
        }
        payload = payload + "}";
        return  payload;
    }


    /**4
     * 根据用户信息获取用户所在区域的设备信息汇总, totalNum:设备总数 offLineNum:离线设备数 errNum:异常设备数 totalData:数据量
     * @param id 用于ID
     * @return
     */
    public Map<String, Object> findTotalDevicesByUserId(Integer id){
        Map<String, Object> result = new HashMap<>();
        result.put("totalNum", deviceMapper.findTotalNumByUserId(id));
        result.put("offLineNum", deviceMapper.findOffLineNumByUserId(id));
        result.put("errNum", deviceMapper.findErrNumByUserId(id));
        result.put("totalData", airLightService.findAllCount() + waterService.findAllCount() + tempAndHumService.findAllCount() + autoReclosingPowerProtectorService.findAllCount());
        return result;
    }


    /**5
     * 修改设备状态,是异步的方法
     * @param device_id 设备号
     * @param status_id 状态号
     */
    @Async
    public void updateStates(String device_id, Integer status_id){
        Map<String,Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("status_id", status_id);
        deviceMapper.updateStateById(data);
    }


    /**6
     * 当设备连接mqtt时，代表设备上线，修改其在数据库中的状态
     * @param device_id 设备号
     */
    @Async
    public void connect(String device_id){
        deviceMapper.connect(device_id);
    }


    /**7
     * 当设备与mqtt断开时，代表设备下线，修改其在数据库中的状态
     * @param device_id 设备号
     */
    @Async
    public void disconnect(String device_id) {
        deviceMapper.disconnect(device_id);
    }


    /**8
     * 设备状态改变时进行处理，判断是从正常变成故障，还是从故障变成正常
     * @param data Map里面包含device_id(String), create_time(String), old_status(String), new_status(String), sensors(List<Map>) sensor(包含id, old_status, new_status, description)
     * status: 1=正常 3=故障 4=异常
     * @return payload Map里面包含device_id(String), status(String)
     */
    public Map<String, Object> handlerStatus(Map<String, Object> data){
        Map<String, Object> payload = new HashMap<>();
        String device_id = (String) data.get("device_id");
        String create_time = (String) data.get("create_time");
        Integer status_id = (Integer) data.get("status_id");
        List<Map<String,Object>> sensors =  (ArrayList)data.get("sensors");
        updateStates(device_id, status_id); //修改设备的状态
        payload.put("device_id", device_id);
        if(status_id == 1){
            payload.put("status", "正常");
        }else{
            payload.put("status", "故障");
        }
        for (Map<String, Object> sensor : sensors) {
            String old_status = (String) sensor.get("old_status");
            String new_status = (String) sensor.get("new_status");
            String description = (String) sensor.get("description");
            if (new_status.equals("1")) { //传感器原本是故障的，后面正常了，要删除该传感器的故障信息
                deleteErr(device_id, new Integer(old_status), description);
            } else { //有新的故障信息到来
                handlerErr(device_id, new Integer(new_status), create_time, description);
            }
        }
        return payload;
    }


    public void handleOrder(Map<String, Object> data){
        String device_id = data.get("device_id").toString();
        List<String> users = findAllUserByDeviceId(device_id);
        //topic 格式为 /queue/order
        for (String user : users) {
            simpMessagingTemplate.convertAndSendToUser(user, "/queue/order", data);
        }
    }

    /**9
     * 处理设备异常的共同方法，先插入数据库，用websocket通知前端，再通过mqtt发送消息创建维修单
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
        //往数据库中插入数据
        insertErr(payload);
        payload.put("status_id", status_id.toString());
        //mqtt通知另一个后台创建维修单
        mqttService.sendToMqtt("user/Order/error",payload.toString());
        //webSocket通知前端，需要发到管理该台设备的用户那里
        List<String> users = findAllUserByDeviceId(device_id);
        payload.remove("status_id");
        payload.put("area_name", locationService.getAreaNameByDevice(device_id));
        //topic 格式为 /queue/error/add
        for (String user : users) {
            simpMessagingTemplate.convertAndSendToUser(user, "/queue/error/add", payload);
        }
    }


    /**10
     * 在数据库中插入故障设备信息
     * @param data Map中要有device_id(String), status_id(Integer类型), date_time(String), description(String)
     * @return
     */
    public Long insertErr(Map<String, Object> data) {
        return deviceMapper.insertErrTables(data);
    }


    /**11
     * 在数据库中删除故障设备信息
     * @param device_id 设备号
     * @param status_id 状态号
     * @param description 描述信息
     */
    public void deleteErr(String device_id, Integer status_id, String description) {
        Map<String, Object> data = new HashMap<>();
        data.put("device_id", device_id);
        data.put("status_id", status_id);
        data.put("description", description);
        //插入数据库
        deviceMapper.deleteErr(data);
        //通知前端故障删除,需要发到特定的用户
        List<String> users = findAllUserByDeviceId(device_id);
        //topic 格式为 /queue/error/remove
        for (String user : users) {
            simpMessagingTemplate.convertAndSendToUser(user, "/queue/error/remove", data);
        }
    }


    /**12
     * 返回管理这台设备的所有管理人员，返回的是用户名
     * @param device_id  设备号
     * @return
     */
    public List<String> findAllUserByDeviceId(String device_id) {
        return userMapper.findAllUserByDeviceId(device_id);
    }


    /**13
     * 根据用户获取其所在区域的所有故障设备信息
     * @param id 用户ID
     * @return
     */
    public List<ErrDevices> findErrDeviceByUserId(Integer id) {
        return deviceMapper.findErrDeviceByUserId(id);
    }

}
