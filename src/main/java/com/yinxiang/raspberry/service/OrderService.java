package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.OrderMapper;
import com.yinxiang.raspberry.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;

    public List<Order> getOrder(String status) {
        return orderMapper.getOrder(status);
    }

    public Map<String,Object> getOrderByDid(String did) {
        Map<String,Object> map = new HashMap<>();
        List<Order> orders = orderMapper.getOrderByDid(did);
        if(orders.isEmpty()) {
            map.put("msg","无此设备" );
            map.put("status","fail" );
        }else {
            map.put("msg",orders );
            map.put("status","success" );
        }
        return map;
    }
}
