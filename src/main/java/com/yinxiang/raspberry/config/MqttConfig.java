package com.yinxiang.raspberry.config;

import com.yinxiang.raspberry.bean.DeviceInformation;
import com.yinxiang.raspberry.service.DeviceInformationService;
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
import org.springframework.util.StringUtils;


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
    private String receiveTopic;

    @Autowired
    DeviceInformationService deviceInformationService;
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
        messageHandler.setDefaultTopic(receiveTopic);
        return messageHandler;
    }

    /**
     * MQTT接收通道(用于接收消息)
     * @return MessageChannel
     */
    @Bean(name = CHANNEL_NAME_IN)
    public MessageChannel mqttInboundChannel(){
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器(用于接收消息)
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String[] str = StringUtils.split(message.getPayload().toString(), " ");
                deviceInformationService.updateStates(str[0], new Integer(str[1]));
                //System.out.println(str[0] + "update status to" + str[1]);
            }
        };
    }

    /**
     * MQTT消息订阅绑定
     * @return MessageProducer
     */
    @Bean
    public MessageProducer inbound(){
        // 可以同时订阅多个Topic
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                serverId, mqttPahoClientFactory(), receiveTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }
}
