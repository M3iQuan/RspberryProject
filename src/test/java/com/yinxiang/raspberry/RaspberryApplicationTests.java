package com.yinxiang.raspberry;

import com.yinxiang.raspberry.Utils.InfluxDbUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RaspberryApplicationTests {
	@Test
	public void contextLoads() {
	}

}
