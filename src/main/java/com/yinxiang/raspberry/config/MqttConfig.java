package com.yinxiang.raspberry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yinxiang.raspberry.bean.Test;
import com.yinxiang.raspberry.service.*;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Configuration
@IntegrationComponentScan
public class MqttConfig {
    @Value("${spring.mqtt.url}")
    private String hostUrl;

    /*@Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;*/

    @Value("${spring.mqtt.client.id}")
    private String clientId;

    @Value("${spring.mqtt.server.id}")
    private String serverId;

    @Value("${spring.mqtt.default.topic")
    private String sendTopic;

    //用于接收设备发送过来的topic
    @Value("${spring.mqtt.server.topic}")
    private String onlineTopic;

    @Autowired
    DeviceInformationService deviceInformationService;
    @Autowired
    TempAndHumService tempAndHumService;
    @Autowired
    WaterService waterService;
    @Autowired
    AutoReclosingPowerProtectorService autoReclosingPowerProtectorService;
    @Autowired
    AirLightService airLightService;
    @Autowired
    Test test;

    /**
     * 订阅的bean名称
     */
    public static final String CHANNEL_NAME_IN = "mqttInboundChannel";

    /**
     * 发布的bean名称
     */
    public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";

    /**
     * MQTT连接器选项
     * @return MqttConnectOptions
     */

    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        /*
        // 设置连接的用户名
        mqttConnectOptions.setUserName(username);
        // 设置连接的密码
        mqttConnectOptions.setPassword(password.toCharArray());*/
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        // 设置超时时间 单位为秒
        mqttConnectOptions.setConnectionTimeout(30);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线，但这个方法并没有重连的机制
        mqttConnectOptions.setKeepAliveInterval(2);
        return mqttConnectOptions;
    }

    /**
     * MQTT 客户端
     * @return MqttPahoClientFactory
     */
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    /**
     * MQTT发送通道(用于发送消息)
     * @return MessageChannel
     */
    @Bean(name = CHANNEL_NAME_OUT)
    public MessageChannel mqttOutboundChannel(){
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器(用于发送消息)
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
    public MessageHandler mqttOutbound(){
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttPahoClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(sendTopic);
        return messageHandler;
    }

    /**
     * MQTT接收通道(用于接收消息)
     * @return MessageChannel
     */
    //在线离线的
    @Bean(name = "mqttInboundChannelOnLine")
    public MessageChannel mqttInboundChannelOnLine(){
        return new DirectChannel();
    }
    //temperature_and_humidity
    @Bean(name = "mqttInboundChannelDataOne")
    public MessageChannel mqttInboundChannelDataOne(){
        return new DirectChannel();
    }
    //air_light
    @Bean(name = "mqttInboundChannelDataTwo")
    public MessageChannel mqttInboundChannelDataTwo(){
        return new DirectChannel();
    }
    //water
    @Bean(name = "mqttInboundChannelDataThree")
    public MessageChannel mqttInboundChannelDataThree(){
        return new DirectChannel();
    }
    //protector
    @Bean(name = "mqttInboundChannelDataFour")
    public MessageChannel mqttInboundChannelDataFour(){
        return new DirectChannel();
    }


    /**
     * MQTT消息处理器(用于接收消息)
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelOnLine")
    public MessageHandler handlerOnLine() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String[] deviceInformation = message.getPayload().toString().split("#");
                System.out.println("device_id: " + deviceInformation[0] + " type_id: " + deviceInformation[1] + " date_time: " + deviceInformation[2] + " status_value: " + deviceInformation[3]);
                deviceInformationService.updateStatesById(deviceInformation[0], deviceInformation[1], deviceInformation[2], deviceInformation[3]);
            }
        };
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelDataOne")
    public MessageHandler handlerDataOne() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                String jsonData = message.getPayload().toString();
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> data = null;
                try {
                    data = mapper.readValue(jsonData, Map.class);
                }catch (IOException e){
                    e.printStackTrace();
                }
                tempAndHumService.saveData(data);
                System.out.println(topic + " receive message " + jsonData);
            }
        };
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelDataTwo")
    public MessageHandler handlerDataTwo() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                String jsonData = message.getPayload().toString();
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> data = null;
                try {
                    data = mapper.readValue(jsonData, Map.class);
                }catch (IOException e){
                    e.printStackTrace();
                }
                airLightService.saveData(data);
                System.out.println(topic + " receive message " + jsonData);
            }
        };
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelDataThree")
    public MessageHandler handlerDataThree() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                String jsonData = message.getPayload().toString();
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> data = null;
                try {
                    data = mapper.readValue(jsonData, Map.class);
                }catch (IOException e){
                    e.printStackTrace();
                }
                waterService.saveData(data);
                System.out.println(topic + " receive message " + jsonData);
            }
        };
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelDataFour")
    public MessageHandler handlerDataFour() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                String jsonData = message.getPayload().toString();
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> data = null;
                try {
                    data = mapper.readValue(jsonData, Map.class);
                }catch (IOException e){
                    e.printStackTrace();
                }
                autoReclosingPowerProtectorService.saveData(data);
                System.out.println(topic + " receive message " + jsonData);
            }
        };
    }




    /**
     * MQTT消息订阅绑定
     * @return MessageProducer
     */
    @Bean
    //判断离线在线的topic
    public MessageProducer inboundOnLine(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                serverId+"_online", mqttPahoClientFactory(), onlineTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelOnLine());
        return adapter;
    }
    @Bean
    //接收temperature_and_humidity的topic
    public MessageProducer inboundDataOne(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                serverId+"_device_temperature_and_humidity", mqttPahoClientFactory(), "device/temperature_and_humidity");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelDataOne());
        return adapter;
    }
    @Bean
    //接收air_light的topic
    public MessageProducer inboundDataTwo(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                serverId+"_device_air_light", mqttPahoClientFactory(), "device/air_light");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelDataTwo());
        return adapter;
    }
    @Bean
    //接收water的topic
    public MessageProducer inboundDataThree(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                serverId+"_device_water", mqttPahoClientFactory(), "device/water");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelDataThree());
        return adapter;
    }
    @Bean
    //接收protector的topic
    public MessageProducer inboundDataFour(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                serverId+"_device_protector", mqttPahoClientFactory(), "device/protector");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelDataFour());
        return adapter;
    }
}
