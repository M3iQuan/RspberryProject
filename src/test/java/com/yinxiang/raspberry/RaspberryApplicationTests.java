package com.yinxiang.raspberry;

import com.yinxiang.raspberry.bean.Area;
import com.yinxiang.raspberry.bean.AutoReclosingPowerProtector;
import com.yinxiang.raspberry.mapper.DevicesMapper;
import com.yinxiang.raspberry.mapper.LocationMapper;
import com.yinxiang.raspberry.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.util.*;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RaspberryApplicationTests {

	@Autowired
	MqttService mqttService;
	@Autowired
	AutoReclosingPowerProtectorService autoReclosingPowerProtectorService;
	@Autowired
	DeviceInformationService deviceInformationService;
	@Autowired
	LocationMapper locationMapper;
	@Autowired
	TempAndHumService tempAndHumService;
	@Autowired
	DevicesMapper devicesMapper;

	@Test
	public void contextLoads() {
		ArrayList<String> data = new ArrayList<>();
		data.add("ns14997");
		data.add("ns14994");
		data.add("ns14993");
		devicesMapper.updateOnlineByList(data);
		/*Map<String, Object> data = new HashMap<>();
		data.put("device_id", "yixg0000001");
		data.put("pageSize", new Integer(10));
		data.put("currentPage", new Integer(1));
		List<Map<String, Object>> queryString = new LinkedList<>();
		Map<String, Object> t1 = new HashMap<>();
		Map<String, Object> t2 = new HashMap<>();
		t1.put("name", "temperature");
		t1.put("opt", "lessEqual");
		t1.put("value", new Integer(30));
		t2.put("name", "humidity");
		t2.put("opt", "moreEqual");
		t2.put("value", new Integer(25));
		queryString.add(t1);
		queryString.add(t2);
		data.put("queryString", queryString);
		tempAndHumService.queryOnCondition(data);*/
		/*HashSet<String> data = new HashSet<>();
		data.add("yixg0000001");
		data.add("ba10112");
		data.add("ba10027");
		data.add("ba10027");
		deviceInformationService.updateOnlineBySet(data);*/
		//System.out.println(deviceInformationService.findSensorsData("yixg0000001"));
		/*String payload = "{\"device_id\":\"yixg0000001"+"\", \"status_id\":3, \"create_time\":\"2019-8-31 9:00:00"+"\", \"description\":\"水浸状态异常\"}";
		mqttService.sendToMqtt("user/error/ns",payload);
		System.out.println(payload);*/
		/*AutoReclosingPowerProtector autoReclosingPowerProtector = autoReclosingPowerProtectorService.findLatestDataById("yixg0000001");
		autoReclosingPowerProtector.setSta_H("分");
		System.out.println("success11111111111111111111111111");
		autoReclosingPowerProtectorService.modifyData(autoReclosingPowerProtector);*/
	}

}
