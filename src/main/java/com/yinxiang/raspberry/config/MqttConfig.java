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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EnableAsync
@Configuration
@IntegrationComponentScan
public class MqttConfig {
    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.username}")
    private String username;

    /*@Value("${spring.mqtt.password}")
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

    private String connectedTopic = "$SYS/brokers/emqx@127.0.0.1/clients/+/connected";
    private String disconnectedTopic = "$SYS/brokers/emqx@127.0.0.1/clients/+/disconnected";
    private String statusTopic = "/device/status";
    private String tem_and_humTopic = "/device/data/temperature_and_humidity";
    private String water_statusTopic = "/device/data/water";
    private String air_lightTopic = "/device/data/air_light";
    private String protectorTopic = "/device/data/protector";


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
    private SimpMessagingTemplate simpMessagingTemplate;
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
        // 设置连接的用户名
        mqttConnectOptions.setUserName(username);
        // 设置连接的密码
        //mqttConnectOptions.setPassword(password.toCharArray());
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
    //在线的
    @Bean(name = "mqttInboundChannelOnLine")
    public MessageChannel mqttInboundChannelOnLine(){
        return new DirectChannel();
    }
    //离线的
    @Bean(name = "mqttInboundChannelOffLine")
    public MessageChannel mqttInboundChannelOffLine(){
        return new DirectChannel();
    }
    //状态变化的
    @Bean(name = "mqttInboundChannelStatus")
    public MessageChannel mqttInboundChannelStatus(){
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
    //water_status
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
    //处理函数
    public static Map<String,Object> jsonString2Map(String content){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = null;
        try{
            data = mapper.readValue(content,Map.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return  data;
    }


    //设备上线事件
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelOnLine")
    public MessageHandler handlerOnLine() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String jsonData = message.getPayload().toString();
                Map<String, Object> data = jsonString2Map(jsonData);
                String device_id = (String)data.get("username");
                if(!"admin".equals(device_id)) {
                    System.out.println("device: " + device_id + " is connected!") ;
                    deviceInformationService.connect(device_id);  //更新数据表
                    data.clear();
                    data.put("device_id", device_id);
                    data.put("status", "正常");
                    simpMessagingTemplate.convertAndSend("/topic/status", data); //使用Websocket发送数据
                }
            }
        };
    }
    //设备下线事件
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelOffLine")
    public MessageHandler handlerOffLine() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String jsonData = message.getPayload().toString();
                Map<String, Object> data = jsonString2Map(jsonData);
                String device_id = (String)data.get("username");
                if(!"admin".equals(device_id)) {
                    System.out.println("device: " + device_id + " is disconnected!") ;
                    //更新数据表
                    deviceInformationService.disconnect(device_id); //更新数据表
                    data.clear();
                    data.put("device_id", device_id);
                    data.put("status", "离线");
                    simpMessagingTemplate.convertAndSend("/topic/status", data);
                }
            }
        };
    }
    //设备状态变化
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelStatus")
    public MessageHandler handlerStatus() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String jsonData = message.getPayload().toString();
                Map<String, Object> data = jsonString2Map(jsonData);
                //Map<String, Object> payload = deviceInformationService.handlerStatus(data);
                //simpMessagingTemplate.convertAndSend("/topic/status", payload);
            }
        };
    }



    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelDataOne")
    public MessageHandler handlerDataOne() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String jsonData = message.getPayload().toString();
                Map<String, Object> data = jsonString2Map(jsonData);
                test.getTemperature_and_humidity_data().add(data);
                //tempAndHumService.saveData(data);
            }
        };
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelDataTwo")
    public MessageHandler handlerDataTwo() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String jsonData = message.getPayload().toString();
                Map<String, Object> data = jsonString2Map(jsonData);
                test.getAir_light_data().add(data);
                //airLightService.saveData(data);
            }
        };
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelDataThree")
    public MessageHandler handlerDataThree() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String jsonData = message.getPayload().toString();
                Map<String, Object> data = jsonString2Map(jsonData);
                test.getWater_data().add(data);
                //waterService.saveData(data);
            }
        };
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannelDataFour")
    public MessageHandler handlerDataFour() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String jsonData = message.getPayload().toString();
                Map<String, Object> data = jsonString2Map(jsonData);
                test.getProtector_data().add(data);
                //autoReclosingPowerProtectorService.saveData(data);
            }
        };
    }




    /**
     * MQTT消息订阅绑定
     * @return MessageProducer
     */

    //在线
    @Bean
    public MessageProducer inboundOnLine(){
        String id = serverId + "_online";
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                id, mqttPahoClientFactory(), connectedTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelOnLine());
        return adapter;
    }
    //离线
    @Bean
    public MessageProducer inboundOffLine(){
        String id = serverId + "_offline";
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                id, mqttPahoClientFactory(), disconnectedTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelOffLine());
        return adapter;
    }
    //状态变化
    @Bean
    public MessageProducer inboundStatus(){
        String id = serverId + "_status";
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                id, mqttPahoClientFactory(), statusTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelStatus());
        return adapter;
    }
    //接收temperature_and_humidity的topic
    @Bean
    public MessageProducer inboundDataOne(){
        String id = serverId + "_device_temperature_and_humidity";
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                id, mqttPahoClientFactory(), /*"device/temperature_and_humidity"*/ tem_and_humTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelDataOne());
        return adapter;
    }
    //接收air_light的topic
    @Bean
    public MessageProducer inboundDataTwo(){
        String id = serverId+"_device_air_light";
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                id, mqttPahoClientFactory(), /*"device/air_light"*/ air_lightTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelDataTwo());
        return adapter;
    }
    //接收water_status的topic
    @Bean
    public MessageProducer inboundDataThree(){
        String id = serverId + "_device_water";
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                id, mqttPahoClientFactory(), /*"device/water"*/ water_statusTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelDataThree());
        return adapter;
    }
    //接收protector的topic
    @Bean
    public MessageProducer inboundDataFour(){
        String id = serverId+"_device_protector";
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                id, mqttPahoClientFactory(), /*"device/protector"*/ protectorTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannelDataFour());
        return adapter;
    }
}
