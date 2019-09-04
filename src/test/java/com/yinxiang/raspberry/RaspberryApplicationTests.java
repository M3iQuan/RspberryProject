package com.yinxiang.raspberry;

import com.yinxiang.raspberry.bean.AutoReclosingPowerProtector;
import com.yinxiang.raspberry.service.AutoReclosingPowerProtectorService;
import com.yinxiang.raspberry.service.MqttService;
import com.yinxiang.raspberry.service.WebClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RaspberryApplicationTests {

	@Autowired
	MqttService mqttService;
	@Autowired
	AutoReclosingPowerProtectorService autoReclosingPowerProtectorService;

	@Test
	public void contextLoads() {
		String payload = "{\"device_id\":\"yixg0000001"+"\", \"status_id\":3, \"create_time\":\"2019-8-31 9:00:00"+"\", \"description\":\"水浸状态异常\"}";
		mqttService.sendToMqtt("user/error/ns",payload);
		System.out.println(payload);
		/*AutoReclosingPowerProtector autoReclosingPowerProtector = autoReclosingPowerProtectorService.findLatestDataById("yixg0000001");
		autoReclosingPowerProtector.setSta_H("分");
		System.out.println("success11111111111111111111111111");
		autoReclosingPowerProtectorService.modifyData(autoReclosingPowerProtector);*/
	}

}
