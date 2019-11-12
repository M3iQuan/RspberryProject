package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.config.MqttSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MqttService {
    @Autowired
    MqttSender mqttSender;

    public void sendToMqtt(String data){
        mqttSender.sendToMqtt(data);
    }

    public void sendToMqtt(String topic, String data){
        mqttSender.sendToMqtt(topic, data);
    }

    public void sendToMqtt(String topic, int qos, String data){
        mqttSender.sendToMqtt(topic, qos, data);
    }
}
