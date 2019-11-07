package com.yinxiang.raspberry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yinxiang.raspberry.Utils.InfluxDbUtils;
import com.yinxiang.raspberry.Utils.MessageUtils;
import com.yinxiang.raspberry.bean.*;
import com.yinxiang.raspberry.service.*;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RaspberryApplicationTests {
	@Autowired
	MqttService mqttService;

	@Test
	public void contextLoads() {
		Map<String, Object> payload= new HashMap<>();
		payload.put("device_id", "ns10139");
		payload.put("status_id", new Integer(3));
		payload.put("create_time", "2019-11-6 11:11:11");
		payload.put("description","ttt");
		System.out.println(MessageUtils.Map2jsonString(payload));
	}
}
