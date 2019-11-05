package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.Utils.InfluxDbUtils;
import com.yinxiang.raspberry.bean.ControlFan;
import com.yinxiang.raspberry.bean.ControlProtector;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RemoteControlService {
    @Autowired
    InfluxDbUtils influxDbUtils;
    @Autowired
    MqttService mqttService;
    private String protector_table = "protector";
    private String fan_table = "temp";


    /**1
     * 返回用户所在的区域的所有设备的重合闸状态Sta_H
     * @param area_ids 区域号集合，根据用户获取
     * @return
     */
    public List<ControlProtector> getAllProtectorStatusByUser(List<String> area_ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("(area_id='" + area_ids.get(0) + "'");
        for (int i = 1; i < area_ids.size(); i++) {
            sql.append(" or area_id='" + area_ids.get(i) + "'");
        }
        sql.append(") ");
        Query query = new Query("SELECT area_id,device_id,Sta_H from " + protector_table + " where " + sql.toString() + " group by device_id order by time desc limit 1");
        QueryResult queryResult = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(queryResult, ControlProtector.class);
    }


    /**2
     * 返回用户所在的区域的所有设备的风扇状态: fan_state(风扇状态), fan_speed(风速), auto_flag(自动控制)
     * @param area_ids 区域号集合，根据用户获取
     * @return
     */
    public List<ControlFan> getAllFanStatusByUser(List<String> area_ids){
        StringBuilder sql = new StringBuilder();
        sql.append("(area_id='" + area_ids.get(0) + "'");
        for (int i = 1; i < area_ids.size(); i++) {
            sql.append(" or area_id='" + area_ids.get(i) + "'");
        }
        sql.append(") ");
        Query query = new Query("SELECT area_id,device_id,fan_state,fan_speed,auto_flag from " + fan_table + " where " + sql.toString() + " group by device_id order by time desc limit 1");
        QueryResult queryResult = influxDbUtils.getInfluxDB().query(query);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(queryResult, ControlFan.class);
    }


    /**3
     * 打开某台设备的风扇
     * @param device_id 设备号
     */
    public void fanOn(String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"fan\",\"value\":\"200\"}";
        //qos = 2代表只有一次，确保信息到达一次
        mqttService.sendToMqtt(topic, 2, data);
    }


    /**4
     * 关闭某台设备的风扇
     * @param device_id 设备号
     */
    public void fanOff(String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"fan\",\"value\":\"0\"}";
        mqttService.sendToMqtt(topic, 2, data);
    }


    /**5
     * 调整某台设备的风速
     * @param device_id 设备号
     * @param fan_speed 风速
     */
    public void fanSpeed(String device_id, String fan_speed) {
        String topic = "/device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"fan\",\"value\":\""+fan_speed+"\"}";
        mqttService.sendToMqtt(topic, data);
    }


    /**6
     * 开启某台设备的自动控制
     * @param device_id 设备号
     */
    public void fanAutoControlOn(String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"fan\",\"value\":\"1000\"}";
        mqttService.sendToMqtt(topic, 2, data);
    }


    /**7
     * 关闭某台设备的自动控制
     * @param device_id 设备号
     */
    public void fanAutoControlOff(String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"fan\",\"value\":\"2000\"}";
        mqttService.sendToMqtt(topic, 2, data);
    }


    /**8
     * 打开某台设备的重合闸
     * @param device_id 设备号
     */
    public void protectorOn(String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"protector\",\"value\":\"1\"}";
        mqttService.sendToMqtt(topic, 2, data);
    }


    /**9
     * 关闭某台设备的重合闸
     * @param device_id 设备号
     */
    public void protectorOff(String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"protector\",\"value\":\"0\"}";
        mqttService.sendToMqtt(topic, 2, data);
    }


    /**10
     * 重启某台设备的重合闸
     * @param device_id 设备号
     */
    public void protectorRestart(String device_id) {
        String topic = "device/remoteControl/"+device_id;
        String data = "{\"sensor\":\"protector\",\"value\":\"2\"}";
        mqttService.sendToMqtt(topic, 2, data);
    }

    /**11
     * 批量控制风扇
     * @param data Map<String,Object> = {"name":"fan/autoControl", "switch": "open/close", "value":"*", "device_ids": ["**", "**"]}
     */
    public void batchControlFan(Map<String, Object> data) {
        String name = data.get("name").toString();
        String operation = data.get("switch").toString();
        String value = data.get("value").toString();
        List<String> device_ids = (ArrayList)data.get("device_ids");
        if(name.equals("fan")){ // 对风扇的操作
            if(value.equals("")){  // value为空字符串，则代表操作为 open/close 而没有进行风速的操作
                if (operation.equals("open")) {  // 打开风扇
                    for (String device_id : device_ids) fanOn(device_id);
                } else if (operation.equals("close")) { //关闭风扇
                    for (String device_id : device_ids) fanOff(device_id);
                }
            }else{  //否则，需要先打开风扇，然后再进行调整风速
                for (String device_id : device_ids) {
                    fanOn(device_id);
                    fanSpeed(device_id, value);
                }
            }
        } else if (name.equals("autoControl")) {  //对自动控制的操作
            if (operation.equals("open")) {
                for(String device_id : device_ids) fanAutoControlOn(device_id);
            } else if (operation.equals("close")) {
                for(String device_id : device_ids) fanAutoControlOff(device_id);
            }
        }
    }


    /**12
     * 批量控制重合闸
     * @param data
     */
    public void batchControlProtector(Map<String, Object> data) {
        String name = data.get("name").toString();
        String operation = data.get("switch").toString();
        String value = data.get("value").toString();
        List<String> device_ids = (ArrayList)data.get("device_ids");
        if (name.equals("protector")) {
            if (operation.equals("open")) {  // 打开重合闸
                for(String device_id : device_ids) protectorOn(device_id);
            } else if (operation.equals("close")) {  // 关闭重合闸
                for(String device_id : device_ids) protectorOff(device_id);
            } else if (operation.equals("restart")) {  // 重启重合闸
                for(String device_id : device_ids) protectorRestart(device_id);
            }
        }
    }
}
